package com.fit2081assignment3.nutritrack.viewmodel.questionnaire

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081assignment3.nutritrack.R
import com.fit2081assignment3.nutritrack.data.model.Nutrition
import com.fit2081assignment3.nutritrack.data.model.User
import com.fit2081assignment3.nutritrack.data.repository.AuthManager
import com.fit2081assignment3.nutritrack.data.repository.NutritionRepository
import com.fit2081assignment3.nutritrack.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionnaireViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authManager: AuthManager,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionnaireUiState())
    val uiState = _uiState.asStateFlow()

    private val foodCategories = listOf(
        "Fruits", "Vegetables", "Grains",
        "Red Meat", "Seafood", "Poultry",
        "Fish", "Eggs", "Nuts/Seeds"
    )

    private val personas = listOf(
        Persona(
            "Health Devotee",
            "I’m passionate about healthy eating...",
            R.drawable.persona_1
        ),
        Persona(
            "Mindful Eater",
            "I’m health-conscious and being healthy...",
            R.drawable.persona_2
        ),
        Persona(
            "Wellness Striver",
            "I aspire to be healthy (but struggle sometimes)...",
            R.drawable.persona_3
        ),
        Persona(
            "Balance Seeker",
            "I try and live a balanced lifestyle...",
            R.drawable.persona_4
        ),
        Persona(
            "Health Procrastinator",
            "I’m contemplating healthy eating...",
            R.drawable.persona_5
        ),
        Persona(
            "Food Carefree",
            "I’m not bothered about healthy eating...",
            R.drawable.persona_6
        )
    )

    init {
        resetCheckStates()
    }

    fun getPersonas() : List<Persona> {
        return personas;
    }

    fun loadQuestionnaireData() {
        viewModelScope.launch(Dispatchers.IO) {
            val (isLoggedIn, currentUserId) = authManager.loginState.first()
            if (currentUserId != null) {
                userRepository.getUser(currentUserId)?.let { user ->
                    _uiState.update {
                        it.copy(
                            checkedStates = parseFoodPreferences(user.foodPreferences),
                            selectedPersona = personas.find { p -> p.title == user.persona },
                            mealTime = user.mealTime ?: "00:00",
                            sleepTime = user.sleepTime ?: "00:00",
                            wakeTime = user.wakeTime ?: "00:00"
                        )
                    }
                }
            }
        }
    }

    fun saveQuestionnaireData() {
        viewModelScope.launch(Dispatchers.IO) {
            val (isLoggedIn, currentUserId) = authManager.loginState.first()
            if (currentUserId != null) {
                userRepository.getUser(currentUserId)?.let { existingUser ->
                    val updatedUser = existingUser.copy(
                        persona = uiState.value.selectedPersona?.title ?: "",
                        mealTime = uiState.value.mealTime,
                        sleepTime = uiState.value.sleepTime,
                        wakeTime = uiState.value.wakeTime,
                        foodPreferences = getSelectedFoods()
                    )

                    // Generate nutritional data
                    val nutrition = generateNutritionData(
                        userId = currentUserId.toInt(),
                        user = updatedUser,
                        questionnaireState = uiState.value
                    )

                    // Save Data
                    userRepository.updateUser(updatedUser)
                    nutritionRepository.insertNutrition(nutrition)
                }
            }
        }
    }

    fun updateCheckState(index: Int, checked: Boolean) {
        _uiState.update { state ->
            val newStates = state.checkedStates.toMutableList().apply {
                set(index, checked)
            }
            state.copy(checkedStates = newStates)
        }
    }

    fun updateSelectedPersona(persona: Persona) {
        _uiState.update { it.copy(selectedPersona = persona) }
    }

    fun updateMealTime(time: String) {
        _uiState.update { it.copy(mealTime = time) }
    }

    fun updateSleepTime(time: String) {
        _uiState.update { it.copy(sleepTime = time) }
    }

    fun updateWakeTime(time: String) {
        _uiState.update { it.copy(wakeTime = time) }
    }

    private fun getSelectedFoods(): String {
        return uiState.value.checkedStates
            .mapIndexed { index, checked ->
                if (checked) foodCategories[index] else null
            }
            .filterNotNull()
            .joinToString(",")
    }

    private fun parseFoodPreferences(prefs: String?): List<Boolean> {
        val selected = prefs?.split(",")?.toSet() ?: emptySet()
        return foodCategories.map { it in selected }
    }

    private fun resetCheckStates() {
        _uiState.update { it.copy(checkedStates = List(9) { false }) }
    }

    // Generate Nutrition Data
    private fun generateNutritionData(
        userId: Int,
        user: User,
        questionnaireState: QuestionnaireUiState
    ): Nutrition {
        // Basic parameters
        val isMale = user.gender.equals("male", ignoreCase = true)
        val foodPrefs = questionnaireState.checkedStates
        val persona = questionnaireState.selectedPersona?.title ?: ""

        // Core algorithm - Generate values based on questionnaire data
        return Nutrition(
            /* ========== Basic information ========== */
            phoneNumber = user.phoneNumber,
            userId = userId,
            sex = user.gender,

            /* ========== HEIFA Total score ========== */
            heifaTotalScoreMale = calculateTotalScore(isMale, foodPrefs, persona, true),
            heifaTotalScoreFemale = calculateTotalScore(isMale, foodPrefs, persona, false),

            /* ========== Free choice of food rating ========== */
            discretionaryHeifaScoreMale = if (isMale) randomInt(5, 10) else 0,
            discretionaryHeifaScoreFemale = if (!isMale) randomInt(5, 10) else 0,
            discretionaryServeSize = calculateServeSize(foodPrefs, 0.2, 1.5),

            /* ========== Vegetable related ratings ========== */
            vegetablesHeifaScoreMale = if (isMale) calculateVegetableScore(foodPrefs) else 0.0,
            vegetablesHeifaScoreFemale = if (!isMale) calculateVegetableScore(foodPrefs) else 0.0,
            vegetablesWithLegumesServeSize = calculateServeSize(foodPrefs, 0.1, 0.5),
            legumesAllocatedVegetables = if (foodPrefs[1]) 1 else 0, // 假设第二个选项是蔬菜
            vegetablesVariationScore = countSelected(foodPrefs, 0..2), // 前3个是蔬菜种类
            vegetablesCruciferous = randomDouble(0.0, 0.5),
            vegetablesTuberAndBulb = randomDouble(0.0, 0.5),
            vegetablesOther = randomDouble(0.0, 0.5),
            legumes = if (foodPrefs[3]) 0.3 else 0.0,
            vegetablesGreen = randomDouble(0.0, 0.5),
            vegetablesRedOrange = randomDouble(0.0, 0.5),

            /* ========== Fruit related ratings ========== */
            fruitHeifaScoreMale = if (isMale) calculateFruitScore(foodPrefs) else 0.0,
            fruitHeifaScoreFemale = if (!isMale) calculateFruitScore(foodPrefs) else 0.0,
            fruitServeSize = calculateServeSize(foodPrefs, 0.1, 0.8),
            fruitVariationScore = countSelected(foodPrefs, 3..5), // 4-6是水果种类
            fruitPome = randomDouble(0.0, 0.5),
            fruitTropical = randomDouble(0.0, 0.5),
            fruitBerry = randomDouble(0.0, 0.5),
            fruitStone = randomDouble(0.0, 0.5),
            fruitCitrus = randomDouble(0.0, 0.5),
            fruitOther = randomDouble(0.0, 0.5),

            /* ========== Grain related ratings ========== */
            grainsCerealsHeifaScoreMale = if (isMale) calculateGrainScore(foodPrefs) else 0.0,
            grainsCerealsHeifaScoreFemale = if (!isMale) calculateGrainScore(foodPrefs) else 0.0,
            grainsCerealsServeSize = calculateServeSize(foodPrefs, 0.5, 3.0),
            nonWholeGrains = randomDouble(0.5, 2.0),
            wholeGrainsHeifaScoreMale = if (isMale) randomDouble(1.0, 3.0) else 0.0,
            wholeGrainsHeifaScoreFemale = if (!isMale) randomDouble(1.0, 3.0) else 0.0,
            wholeGrainsServeSize = calculateServeSize(foodPrefs, 0.3, 1.5),

            /* ========== Meat and its substitutes ========== */
            meatAlternativesHeifaScoreMale = if (isMale) randomInt(4, 10) else 0,
            meatAlternativesHeifaScoreFemale = if (!isMale) randomInt(4, 10) else 0,
            meatAlternativesServeSize = calculateServeSize(foodPrefs, 0.5, 2.0),
            legumesAllocatedMeat = if (foodPrefs[6]) 1 else 0, // 假设第七个选项是肉类替代品

            /* ========== Dairy products and substitutes ========== */
            dairyAlternativesHeifaScoreMale = if (isMale) randomInt(5, 10) else 0,
            dairyAlternativesHeifaScoreFemale = if (!isMale) randomInt(5, 10) else 0,
            dairyAlternativesServeSize = calculateServeSize(foodPrefs, 0.3, 1.8),

            /* ========== Sodium related data ========== */
            sodiumHeifaScoreMale = if (isMale) randomInt(5, 10) else 0,
            sodiumHeifaScoreFemale = if (!isMale) randomInt(5, 10) else 0,
            sodiumMg = randomDouble(500.0, 3000.0),

            /* ========== Alcohol related data ========== */
            alcoholHeifaScoreMale = if (isMale) randomInt(0, 5) else 0,
            alcoholHeifaScoreFemale = if (!isMale) randomInt(0, 5) else 0,
            alcoholStandardDrinks = randomDouble(0.0, 3.0),

            /* ========== Water related data ========== */
            waterHeifaScoreMale = if (isMale) randomInt(5, 10) else 0,
            waterHeifaScoreFemale = if (!isMale) randomInt(5, 10) else 0,
            water = randomDouble(500.0, 3000.0),
            waterTotalMl = randomDouble(1000.0, 4000.0),
            beverageTotalMl = randomDouble(500.0, 2000.0),

            /* ========== Sugar related data ========== */
            sugarHeifaScoreMale = if (isMale) randomInt(5, 10) else 0,
            sugarHeifaScoreFemale = if (!isMale) randomInt(5, 10) else 0,
            sugar = randomDouble(10.0, 100.0),

            /* ========== Fat related data ========== */
            saturatedFatHeifaScoreMale = if (isMale) randomInt(2, 5) else 0,
            saturatedFatHeifaScoreFemale = if (!isMale) randomInt(2, 5) else 0,
            saturatedFat = randomDouble(10.0, 50.0),
            unsaturatedFatHeifaScoreMale = if (isMale) randomInt(2, 5) else 0,
            unsaturatedFatHeifaScoreFemale = if (!isMale) randomInt(2, 5) else 0,
            unsaturatedFatServeSize = randomDouble(0.5, 5.0)
        )
    }

    // Auxiliary calculation function
    private fun calculateTotalScore(
        isMale: Boolean,
        foodPrefs: List<Boolean>,
        persona: String,
        forMale: Boolean
    ): Double {
        val baseScore = when (persona) {
            "Health Devotee" -> 45.0
            "Mindful Eater" -> 40.0
            "Wellness Striver" -> 35.0
            "Balance Seeker" -> 30.0
            else -> 25.0
        }

        val foodBonus = foodPrefs.count { it } * 1.5
        val genderFactor = if (forMale) 1.05 else 1.03
        return (baseScore + foodBonus) * genderFactor
    }

    private fun calculateVegetableScore(foodPrefs: List<Boolean>): Double {
        return foodPrefs.slice(0..2).count { it } * 2.5
    }

    private fun calculateFruitScore(foodPrefs: List<Boolean>): Double {
        return foodPrefs.slice(3..5).count { it } * 1.8
    }

    private fun calculateGrainScore(foodPrefs: List<Boolean>): Double {
        return foodPrefs.slice(6..8).count { it } * 1.2
    }

    private fun calculateServeSize(
        foodPrefs: List<Boolean>,
        minPerItem: Double,
        maxPerItem: Double
    ): Double {
        val count = foodPrefs.count { it }
        return count * randomDouble(minPerItem, maxPerItem)
    }

    private fun countSelected(prefs: List<Boolean>, range: IntRange): Int {
        return prefs.slice(range).count { it }
    }

    private fun randomDouble(min: Double, max: Double): Double {
        return min + (max - min) * Math.random()
    }

    private fun randomInt(min: Int, max: Int): Int {
        return (min..max).random()
    }
}

data class QuestionnaireUiState(
    val checkedStates: List<Boolean> = emptyList(),
    val selectedPersona: Persona? = null,
    val mealTime: String = "00:00",
    val sleepTime: String = "00:00",
    val wakeTime: String = "00:00"
)

data class Persona(
    val title: String,
    val description: String,
    val imageResId: Int
)