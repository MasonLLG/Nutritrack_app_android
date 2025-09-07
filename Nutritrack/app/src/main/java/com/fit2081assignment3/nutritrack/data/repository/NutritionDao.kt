package com.fit2081assignment3.nutritrack.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fit2081assignment3.nutritrack.data.model.Nutrition

@Dao
interface NutritionDao {
    @Insert
    fun insertAll(data: List<Nutrition>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(nutrition: Nutrition)

    @Query("SELECT * FROM nutrition WHERE User_ID = :userId LIMIT 1")
    fun getByUserId(userId: Int): Nutrition?
}