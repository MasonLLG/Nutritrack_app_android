package com.fit2081assignment3.nutritrack.data.model

data class Fruit(
    val id: Int,
    val name: String,
    val family: String,
    val genus: String,
    val order: String,
    val nutritions: NutritionData
)

data class NutritionData(
    val calories: Int,
    val fat: Double,
    val sugar: Double,
    val carbohydrates: Double,
    val protein: Double
)