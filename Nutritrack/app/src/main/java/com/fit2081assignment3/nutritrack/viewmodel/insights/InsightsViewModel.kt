package com.fit2081assignment3.nutritrack.viewmodel.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081assignment3.nutritrack.data.model.Nutrition
import com.fit2081assignment3.nutritrack.data.model.User
import com.fit2081assignment3.nutritrack.data.repository.UserRepository
import com.fit2081assignment3.nutritrack.data.repository.AuthManager
import com.fit2081assignment3.nutritrack.data.repository.NutritionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authManager: AuthManager,
    private val nutritionRepository: NutritionRepository
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()
    fun loadUser() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = authManager.loginState.first().second
                val fetchedUser = userRepository.getUser(userId.toString())
                _user.value = fetchedUser
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val _nutrition = MutableStateFlow<Nutrition?>(null)
    val nutrition: StateFlow<Nutrition?> = _nutrition.asStateFlow()
    fun loadNutrition() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val userId = authManager.loginState.first().second ?: run {
                    _nutrition.value = null
                    return@launch
                }

                val userIdInt = userId.toIntOrNull() ?: run {
                    _nutrition.value = null
                    return@launch
                }

                val fetchedNutrition = nutritionRepository.getByUserId(userIdInt)
                _nutrition.value = fetchedNutrition
            } catch (e: Exception) {
                _nutrition.value = null
                e.printStackTrace()
            }
        }
    }

    fun getScoreByKey(key: String): Float {
        val nutrition = _nutrition.value ?: return 0.0f

        val fieldMap = mapOf<String, (Nutrition) -> Number>(
            // Total score
            "HEIFAtotalscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.heifaTotalScoreMale
                else n.heifaTotalScoreFemale
            },

            "DiscretionaryHEIFAscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.discretionaryHeifaScoreMale
                else n.discretionaryHeifaScoreFemale
            },

            "VegetablesHEIFAscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.vegetablesHeifaScoreMale
                else n.vegetablesHeifaScoreFemale
            },

            "FruitHEIFAscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.fruitHeifaScoreMale
                else n.fruitHeifaScoreFemale
            },

            "GrainsandcerealsHEIFAscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.grainsCerealsHeifaScoreMale
                else n.grainsCerealsHeifaScoreFemale
            },

            "WholegrainsHEIFAscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.wholeGrainsHeifaScoreMale
                else n.wholeGrainsHeifaScoreFemale
            },

            "MeatandalternativesHEIFAscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.meatAlternativesHeifaScoreMale
                else n.meatAlternativesHeifaScoreFemale
            },

            "DairyandalternativesHEIFAscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.dairyAlternativesHeifaScoreMale
                else n.dairyAlternativesHeifaScoreFemale
            },

            "WaterHEIFAscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.waterHeifaScoreMale
                else n.waterHeifaScoreFemale
            },

            "UnsaturatedFatHEIFAscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.unsaturatedFatHeifaScoreMale
                else n.unsaturatedFatHeifaScoreFemale
            },

            "SodiumHEIFAscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.sodiumHeifaScoreMale
                else n.sodiumHeifaScoreFemale
            },

            "SugarHEIFAscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.sugarHeifaScoreMale
                else n.sugarHeifaScoreFemale
            },

            "AlcoholHEIFAscore" to { n ->
                if (n.sex.equals("Male", ignoreCase = true)) n.alcoholHeifaScoreMale
                else n.alcoholHeifaScoreFemale
            }
        )

        return fieldMap[key]?.invoke(nutrition)?.toFloat() ?: 0.0f
    }
}