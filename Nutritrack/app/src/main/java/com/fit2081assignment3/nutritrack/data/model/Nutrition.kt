package com.fit2081assignment3.nutritrack.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutrition")
data class Nutrition(
    /* ========== 基础信息 ========== */
    @ColumnInfo(name = "PhoneNumber")
    val phoneNumber: String,

    @PrimaryKey
    @ColumnInfo(name = "User_ID")
    val userId: Int,

    @ColumnInfo(name = "Sex")
    val sex: String,

    /* ========== HEIFA Total Score ========== */
    @ColumnInfo(name = "HEIFAtotalscoreMale")
    val heifaTotalScoreMale: Double,

    @ColumnInfo(name = "HEIFAtotalscoreFemale")
    val heifaTotalScoreFemale: Double,

    /* ========== Free choice of food rating ========== */
    @ColumnInfo(name = "DiscretionaryHEIFAscoreMale")
    val discretionaryHeifaScoreMale: Int,

    @ColumnInfo(name = "DiscretionaryHEIFAscoreFemale")
    val discretionaryHeifaScoreFemale: Int,

    @ColumnInfo(name = "Discretionaryservesize")
    val discretionaryServeSize: Double,

    /* ========== Vegetable related ratings ========== */
    @ColumnInfo(name = "VegetablesHEIFAscoreMale")
    val vegetablesHeifaScoreMale: Double,

    @ColumnInfo(name = "VegetablesHEIFAscoreFemale")
    val vegetablesHeifaScoreFemale: Double,

    @ColumnInfo(name = "Vegetableswithlegumesallocatedservesize")
    val vegetablesWithLegumesServeSize: Double,

    @ColumnInfo(name = "LegumesallocatedVegetables")
    val legumesAllocatedVegetables: Int,

    @ColumnInfo(name = "Vegetablesvariationsscore")
    val vegetablesVariationScore: Int,

    @ColumnInfo(name = "VegetablesCruciferous")
    val vegetablesCruciferous: Double,

    @ColumnInfo(name = "VegetablesTuberandbulb")
    val vegetablesTuberAndBulb: Double,

    @ColumnInfo(name = "VegetablesOther")
    val vegetablesOther: Double,

    @ColumnInfo(name = "Legumes")
    val legumes: Double,

    @ColumnInfo(name = "VegetablesGreen")
    val vegetablesGreen: Double,

    @ColumnInfo(name = "VegetablesRedandorange")
    val vegetablesRedOrange: Double,

    /* ========== Fruit related ratings ========== */
    @ColumnInfo(name = "FruitHEIFAscoreMale")
    val fruitHeifaScoreMale: Double,

    @ColumnInfo(name = "FruitHEIFAscoreFemale")
    val fruitHeifaScoreFemale: Double,

    @ColumnInfo(name = "Fruitservesize")
    val fruitServeSize: Double,

    @ColumnInfo(name = "Fruitvariationsscore")
    val fruitVariationScore: Int,

    @ColumnInfo(name = "FruitPome")
    val fruitPome: Double,

    @ColumnInfo(name = "FruitTropicalandsubtropical")
    val fruitTropical: Double,

    @ColumnInfo(name = "FruitBerry")
    val fruitBerry: Double,

    @ColumnInfo(name = "FruitStone")
    val fruitStone: Double,

    @ColumnInfo(name = "FruitCitrus")
    val fruitCitrus: Double,

    @ColumnInfo(name = "FruitOther")
    val fruitOther: Double,

    /* ========== Grain related ratings ========== */
    @ColumnInfo(name = "GrainsandcerealsHEIFAscoreMale")
    val grainsCerealsHeifaScoreMale: Double,

    @ColumnInfo(name = "GrainsandcerealsHEIFAscoreFemale")
    val grainsCerealsHeifaScoreFemale: Double,

    @ColumnInfo(name = "Grainsandcerealsservesize")
    val grainsCerealsServeSize: Double,

    @ColumnInfo(name = "GrainsandcerealsNonwholegrains")
    val nonWholeGrains: Double,

    @ColumnInfo(name = "WholegrainsHEIFAscoreMale")
    val wholeGrainsHeifaScoreMale: Double,

    @ColumnInfo(name = "WholegrainsHEIFAscoreFemale")
    val wholeGrainsHeifaScoreFemale: Double,

    @ColumnInfo(name = "Wholegrainsservesize")
    val wholeGrainsServeSize: Double,

    /* ========== Meat and its substitutes ========== */
    @ColumnInfo(name = "MeatandalternativesHEIFAscoreMale")
    val meatAlternativesHeifaScoreMale: Int,

    @ColumnInfo(name = "MeatandalternativesHEIFAscoreFemale")
    val meatAlternativesHeifaScoreFemale: Int,

    @ColumnInfo(name = "Meatandalternativeswithlegumesallocatedservesize")
    val meatAlternativesServeSize: Double,

    @ColumnInfo(name = "LegumesallocatedMeatandalternatives")
    val legumesAllocatedMeat: Int,

    /* ========== Dairy products and substitutes ========== */
    @ColumnInfo(name = "DairyandalternativesHEIFAscoreMale")
    val dairyAlternativesHeifaScoreMale: Int,

    @ColumnInfo(name = "DairyandalternativesHEIFAscoreFemale")
    val dairyAlternativesHeifaScoreFemale: Int,

    @ColumnInfo(name = "Dairyandalternativesservesize")
    val dairyAlternativesServeSize: Double,

    /* ========== Sodium related data ========== */
    @ColumnInfo(name = "SodiumHEIFAscoreMale")
    val sodiumHeifaScoreMale: Int,

    @ColumnInfo(name = "SodiumHEIFAscoreFemale")
    val sodiumHeifaScoreFemale: Int,

    @ColumnInfo(name = "Sodiummgmilligrams")
    val sodiumMg: Double,

    /* ========== Alcohol related data ========== */
    @ColumnInfo(name = "AlcoholHEIFAscoreMale")
    val alcoholHeifaScoreMale: Int,

    @ColumnInfo(name = "AlcoholHEIFAscoreFemale")
    val alcoholHeifaScoreFemale: Int,

    @ColumnInfo(name = "Alcoholstandarddrinks")
    val alcoholStandardDrinks: Double,

    /* ========== Water related data ========== */
    @ColumnInfo(name = "WaterHEIFAscoreMale")
    val waterHeifaScoreMale: Int,

    @ColumnInfo(name = "WaterHEIFAscoreFemale")
    val waterHeifaScoreFemale: Int,

    @ColumnInfo(name = "Water")
    val water: Double,

    @ColumnInfo(name = "WaterTotalmL")
    val waterTotalMl: Double,

    @ColumnInfo(name = "BeverageTotalmL")
    val beverageTotalMl: Double,

    /* ========== Sugar related data ========== */
    @ColumnInfo(name = "SugarHEIFAscoreMale")
    val sugarHeifaScoreMale: Int,

    @ColumnInfo(name = "SugarHEIFAscoreFemale")
    val sugarHeifaScoreFemale: Int,

    @ColumnInfo(name = "Sugar")
    val sugar: Double,

    /* ========== Fat related data ========== */
    @ColumnInfo(name = "SaturatedFatHEIFAscoreMale")
    val saturatedFatHeifaScoreMale: Int,

    @ColumnInfo(name = "SaturatedFatHEIFAscoreFemale")
    val saturatedFatHeifaScoreFemale: Int,

    @ColumnInfo(name = "SaturatedFat")
    val saturatedFat: Double,

    @ColumnInfo(name = "UnsaturatedFatHEIFAscoreMale")
    val unsaturatedFatHeifaScoreMale: Int,

    @ColumnInfo(name = "UnsaturatedFatHEIFAscoreFemale")
    val unsaturatedFatHeifaScoreFemale: Int,

    @ColumnInfo(name = "UnsaturatedFatservesize")
    val unsaturatedFatServeSize: Double
)