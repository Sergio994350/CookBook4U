package com.sergio994350.cookbook4u.api

import com.sergio994350.cookbook4u.Model.CategoriesList
import com.sergio994350.cookbook4u.Model.RandomMeals
import com.sergio994350.cookbook4u.util.Constants.Companion.CATEGORIES
import com.sergio994350.cookbook4u.util.Constants.Companion.FILTER
import com.sergio994350.cookbook4u.util.Constants.Companion.LOOKUP
import com.sergio994350.cookbook4u.util.Constants.Companion.SEARCH
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApi {

    @GET(SEARCH)
    suspend fun getSearchMealRf(@Query("s") search: String = "lak"): Response<RandomMeals>

    @GET(CATEGORIES)
    suspend fun getCategoryRf(): Response<CategoriesList>

    @GET(FILTER)
    suspend fun getFilterCategoryRf(@Query("c") category: String = "Chicken"): Response<RandomMeals>

    @GET(LOOKUP)
    suspend fun getDetailsRf(@Query("i") id: String = "1"): Response<RandomMeals>
}