package com.sergio994350.cookbook4u.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergio994350.cookbook4u.Model.CategoriesList
import com.sergio994350.cookbook4u.Model.Meal
import com.sergio994350.cookbook4u.Model.RandomMeals
import com.sergio994350.cookbook4u.repository.FoodRepository
import com.sergio994350.cookbook4u.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class FoodViewModel(val foodRepository: FoodRepository) : ViewModel() {
    val tag = "A"
    val searchMeals: MutableLiveData<Resource<RandomMeals>> = MutableLiveData()
    val filter: MutableLiveData<Resource<RandomMeals>> = MutableLiveData()
    val details: MutableLiveData<Resource<RandomMeals>> = MutableLiveData()
    val categoryFood: MutableLiveData<Resource<CategoriesList>> = MutableLiveData()
    var randomResponse: RandomMeals? = null

    init {
        getCategory()
        getFilter("Beef")
    }

    fun getSearchMeal(search: String) = viewModelScope.launch {
        searchMeals.postValue(Resource.Loading())
        val response = foodRepository.getSearchMeal(search)
        searchMeals.postValue(handleRandomFood(response))
    }

    fun getCategory() = viewModelScope.launch {
        categoryFood.postValue(Resource.Loading())
        val response = foodRepository.getCategory()
        categoryFood.postValue(handleCategoryFood(response))
    }

    fun getFilter(category: String) = viewModelScope.launch {
        filter.postValue(Resource.Loading())
        val response = foodRepository.getFilter(category)
        filter.postValue(handleFilterFood(response))
    }

    fun getDetails(id: String) = viewModelScope.launch {
        details.postValue(Resource.Loading())
        val response = foodRepository.getDetails(id)
        details.postValue(handleDetailsFood(response))
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

}