package com.fit2081assignment3.nutritrack.data

import android.content.Context
import android.content.SharedPreferences
import com.fit2081assignment3.nutritrack.data.model.Nutrition
import com.fit2081assignment3.nutritrack.data.model.User
import com.fit2081assignment3.nutritrack.data.repository.NutritionDao
import com.fit2081assignment3.nutritrack.data.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class DatabaseInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val nutritionDao: NutritionDao,
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val PREF_FIRST_RUN = "is_first_run"
        private const val CSV_FILENAME = "data.csv"
    }

    fun initialize() {
        if (sharedPreferences.getBoolean(PREF_FIRST_RUN, true)) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val data = parseCsv()
                    if (data.isNotEmpty()) {
                        nutritionDao.insertAll(data)

                        // Batch create users
                        data.map { nutrition ->
                            User(
                                userId = nutrition.userId.toString(),
                                username = nutrition.userId.toString(),
                                gender = nutrition.sex,
                                phoneNumber = nutrition.phoneNumber,
                                password = nutrition.phoneNumber
                            )
                        }.onEach { user ->
                            userRepository.createUser(user)
                        }

                        sharedPreferences.edit().putBoolean(PREF_FIRST_RUN, false).apply()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private suspend fun parseCsv(): List<Nutrition> = withContext(Dispatchers.IO) {
        try {
            context.assets.open(CSV_FILENAME).bufferedReader().use { reader ->
                val lines = reader.readLines()
                if (lines.size < 2) return@withContext emptyList()

                lines.drop(1).mapNotNull { line ->
                    try {
                        val parts = line.split(",")
                        if (parts.size < 63) return@mapNotNull null

                        Nutrition(
                            // ===== Basic information =====
                            phoneNumber = parts[0],
                            userId = parts[1].toInt(),
                            sex = parts[2],

                            // ===== HEIFA Total Score =====
                            heifaTotalScoreMale = parts[3].toDouble(),
                            heifaTotalScoreFemale = parts[4].toDouble(),

                            // ===== Free choice of food rating =====
                            discretionaryHeifaScoreMale = parts[5].toInt(),
                            discretionaryHeifaScoreFemale = parts[6].toInt(),
                            discretionaryServeSize = parts[7].toDouble(),

                            // ===== Vegetable related =====
                            vegetablesHeifaScoreMale = parts[8].toDouble(),
                            vegetablesHeifaScoreFemale = parts[9].toDouble(),
                            vegetablesWithLegumesServeSize = parts[10].toDouble(),
                            legumesAllocatedVegetables = parts[11].toInt(),
                            vegetablesVariationScore = parts[12].toInt(),
                            vegetablesCruciferous = parts[13].toDouble(),
                            vegetablesTuberAndBulb = parts[14].toDouble(),
                            vegetablesOther = parts[15].toDouble(),
                            legumes = parts[16].toDouble(),
                            vegetablesGreen = parts[17].toDouble(),
                            vegetablesRedOrange = parts[18].toDouble(),

                            // ===== Fruit related =====
                            fruitHeifaScoreMale = parts[19].toDouble(),
                            fruitHeifaScoreFemale = parts[20].toDouble(),
                            fruitServeSize = parts[21].toDouble(),
                            fruitVariationScore = parts[22].toInt(),
                            fruitPome = parts[23].toDouble(),
                            fruitTropical = parts[24].toDouble(),
                            fruitBerry = parts[25].toDouble(),
                            fruitStone = parts[26].toDouble(),
                            fruitCitrus = parts[27].toDouble(),
                            fruitOther = parts[28].toDouble(),

                            // ===== Grain related =====
                            grainsCerealsHeifaScoreMale = parts[29].toDouble(),
                            grainsCerealsHeifaScoreFemale = parts[30].toDouble(),
                            grainsCerealsServeSize = parts[31].toDouble(),
                            nonWholeGrains = parts[32].toDouble(),
                            wholeGrainsHeifaScoreMale = parts[33].toDouble(),
                            wholeGrainsHeifaScoreFemale = parts[34].toDouble(),
                            wholeGrainsServeSize = parts[35].toDouble(),

                            // ===== Meat and its substitutes =====
                            meatAlternativesHeifaScoreMale = parts[36].toInt(),
                            meatAlternativesHeifaScoreFemale = parts[37].toInt(),
                            meatAlternativesServeSize = parts[38].toDouble(),
                            legumesAllocatedMeat = parts[39].toInt(),

                            // ===== Dairy related products =====
                            dairyAlternativesHeifaScoreMale = parts[40].toInt(),
                            dairyAlternativesHeifaScoreFemale = parts[41].toInt(),
                            dairyAlternativesServeSize = parts[42].toDouble(),

                            // ===== Sodium related =====
                            sodiumHeifaScoreMale = parts[43].toInt(),
                            sodiumHeifaScoreFemale = parts[44].toInt(),
                            sodiumMg = parts[45].toDouble(),

                            // ===== Alcohol related =====
                            alcoholHeifaScoreMale = parts[46].toInt(),
                            alcoholHeifaScoreFemale = parts[47].toInt(),
                            alcoholStandardDrinks = parts[48].toDouble(),

                            // ===== Water related =====
                            waterHeifaScoreMale = parts[49].toInt(),
                            waterHeifaScoreFemale = parts[50].toInt(),
                            water = parts[51].toDouble(),
                            waterTotalMl = parts[52].toDouble(),
                            beverageTotalMl = parts[53].toDouble(),

                            // ===== Sugar related =====
                            sugarHeifaScoreMale = parts[54].toInt(),
                            sugarHeifaScoreFemale = parts[55].toInt(),
                            sugar = parts[56].toDouble(),

                            // ===== Fat related =====
                            saturatedFatHeifaScoreMale = parts[57].toInt(),
                            saturatedFatHeifaScoreFemale = parts[58].toInt(),
                            saturatedFat = parts[59].toDouble(),
                            unsaturatedFatHeifaScoreMale = parts[60].toInt(),
                            unsaturatedFatHeifaScoreFemale = parts[61].toInt(),
                            unsaturatedFatServeSize = parts[62].toDouble()
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }
                }.filterNotNull()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
}