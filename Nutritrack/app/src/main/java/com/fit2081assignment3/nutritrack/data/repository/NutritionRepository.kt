package com.fit2081assignment3.nutritrack.data.repository

import com.fit2081assignment3.nutritrack.data.model.Nutrition
import javax.inject.Inject

class NutritionRepository @Inject constructor(
    private val nutritionDao: NutritionDao
) {
    suspend fun getByUserId(userId: Int): Nutrition? = nutritionDao.getByUserId(userId)

    suspend fun insertNutrition(nutrition: Nutrition) = nutritionDao.insert(nutrition)
}