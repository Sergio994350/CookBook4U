package com.sergio994350.cookbook4u.repository

import com.sergio994350.cookbook4u.api.RetrofitInstance
import com.sergio994350.cookbook4u.db.FoodDatabase
import com.sergio994350.cookbook4u.Model.Meal

class FoodRepository(val db: FoodDatabase) {

    // network
    suspend fun getSearchMeal(search: String) = RetrofitInstance.api.getSearchMealRf(search)
    suspend fun getCategory() = RetrofitInstance.api.getCategoryRf()
    suspend fun getFilter(category: String) = RetrofitInstance.api.getFilterCategoryRf(category)
    suspend fun getDetails(id: String) = RetrofitInstance.api.getDetailsRf(id)

    // database
    suspend fun upsert(meal: Meal) = db.getFoodDao().upsert(meal)
    fun getAllFood() = db.getFoodDao().getAllFood()
    suspend fun deleteFood(meal: Meal) = db.getFoodDao().deleteFood(meal)

}