package com.sergio994350.cookbook4u.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sergio994350.cookbook4u.Model.Meal

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal: Meal): Long

    @Query("SELECT * FROM food_table")
    fun getAllFood(): LiveData<List<Meal>>

    @Delete
    suspend fun deleteFood(meal: Meal)
}