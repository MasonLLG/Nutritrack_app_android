package com.fit2081assignment3.nutritrack.viewmodel.login

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.fit2081assignment3.nutritrack.data.repository.UserRepository
import com.fit2081assignment3.nutritrack.data.repository.AuthManager
import com.fit2081assignment3.nutritrack.data.repository.NutritionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authManager: AuthManager,
    private val nutritionDao: NutritionDao
) : ViewModel() {
    var userId by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set

    fun updateUserId(value: String) { userId = value }
    fun updatePassword(value: String) { password = value }

    @SuppressLint("NewApi")
    fun login(): Flow<LoginResult> = flow {
        try {
            isLoading = true
            val user = userRepository.getUser(userId)
                ?: throw Exception("User not found")

            if (user.password != password) {
                throw Exception("Invalid password")
            }

            authManager.saveLoginState(userId)
            emit(LoginResult.Success)
        } catch (e: Exception) {
            emit(LoginResult.Error(e.message ?: "Login failed"))
        } finally {
            isLoading = false
        }
    }.flowOn(Dispatchers.IO)

    suspend fun hasNutritionData(userId: String): Boolean {
        return try {
            val userIdInt = userId.toInt()
            withContext(Dispatchers.IO) {
                nutritionDao.getByUserId(userIdInt) != null
            }
        } catch (e: NumberFormatException) {
            false
        }
    }
}

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}