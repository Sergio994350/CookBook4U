package com.sergio994350.cookbook4u.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sergio994350.cookbook4u.CookBookApplication
import com.sergio994350.cookbook4u.Model.CategoriesList
import com.sergio994350.cookbook4u.Model.Meal
import com.sergio994350.cookbook4u.Model.RandomMeals
import com.sergio994350.cookbook4u.repository.FoodRepository
import com.sergio994350.cookbook4u.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class FoodViewModel(
    app: Application,
    val foodRepository: FoodRepository
) : AndroidViewModel(app) {

    val tag = "A"
    val searchMeals: MutableLiveData<Resource<RandomMeals>> = MutableLiveData()
    val filter: MutableLiveData<Resource<RandomMeals>> = MutableLiveData()
    val details: MutableLiveData<Resource<RandomMeals>> = MutableLiveData()
    val categoryFood: MutableLiveData<Resource<CategoriesList>> = MutableLiveData()
    var randomResponse: RandomMeals? = null

    init {
        getCategory()
        getFilter("Chicken")
    }

    fun getSearchMeal(search: String) = viewModelScope.launch {
        safeGetSearchMeal(search)
    }

    fun getCategory() = viewModelScope.launch {
        safeGetCategory()
    }

    fun getFilter(category: String) = viewModelScope.launch {
        safeGetFilter(category)
    }

    fun getDetails(id: String) = viewModelScope.launch {
        safeGetDetails(id)
    }

    private fun handleDetailsFood(response: Response<RandomMeals>): Resource<RandomMeals>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun handleFilterFood(response: Response<RandomMeals>): Resource<RandomMeals>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleCategoryFood(response: Response<CategoriesList>): Resource<CategoriesList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleRandomFood(response: Response<RandomMeals>): Resource<RandomMeals> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveFood(meal: Meal) = viewModelScope.launch {
        foodRepository.upsert(meal)
    }

    fun getAllFood() = foodRepository.getAllFood()

    fun deleteFood(meal: Meal) = viewModelScope.launch {
        foodRepository.deleteFood(meal)
    }

    private suspend fun safeGetCategory() {
        categoryFood.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getCategory()
                categoryFood.postValue(handleCategoryFood(response))
            } else {
                categoryFood.postValue(Resource.Error("No internet connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> categoryFood.postValue(Resource.Error("Network failure"))
                else -> categoryFood.postValue(Resource.Error("Conversion error"))
            }
        }
    }

    private suspend fun safeGetFilter(category: String) {
        filter.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getFilter(category)
                filter.postValue(handleFilterFood(response))
            } else {
                filter.postValue(Resource.Error("No internet connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> filter.postValue(Resource.Error("Network failure"))
                else -> filter.postValue(Resource.Error("Conversion error"))
            }
        }
    }

    private suspend fun safeGetSearchMeal(search: String) {
        searchMeals.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getSearchMeal(search)
                searchMeals.postValue(handleRandomFood(response))
            } else {
                searchMeals.postValue(Resource.Error("No internet connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchMeals.postValue(Resource.Error("Network failure"))
                else -> searchMeals.postValue(Resource.Error("Conversion error"))
            }
        }
    }

    private suspend fun safeGetDetails(id: String) {
        details.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = foodRepository.getDetails(id)
                details.postValue(handleDetailsFood(response))
            } else {
                details.postValue(Resource.Error("No internet connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> details.postValue(Resource.Error("Network failure"))
                else -> details.postValue(Resource.Error("Conversion error"))
            }
        }
    }

    // function for checking internet connection for API>=23 and <23
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<CookBookApplication>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}