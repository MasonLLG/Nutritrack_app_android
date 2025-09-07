package com.fit2081assignment3.nutritrack.viewmodel.home

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
class HomeViewModel @Inject constructor(
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
}