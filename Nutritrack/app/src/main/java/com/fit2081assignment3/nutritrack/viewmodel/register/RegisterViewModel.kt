package com.fit2081assignment3.nutritrack.viewmodel.register

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.fit2081assignment3.nutritrack.data.repository.UserRepository
import com.fit2081assignment3.nutritrack.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var userId by mutableStateOf("")
        private set
    var phoneNumber by mutableStateOf("")
        private set
    var username by mutableStateOf("")
        private set
    var gender by mutableStateOf("Female")
        private set
    var password by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    fun updateUserId(value: String) { userId = value }
    fun updatePhoneNumber(value: String) { phoneNumber = value }
    fun updateUsername(value: String) { username = value }
    fun updateGender(value: String) { gender = value }
    fun updatePassword(value: String) { password = value }
    fun updateConfirmPassword(value: String) { confirmPassword = value }

    @SuppressLint("NewApi")
    fun register(): Flow<RegisterResult> = flow {
        if (password != confirmPassword) {
            emit(RegisterResult.Error("Passwords do not match"))
        } else {
            try {
                val existingUser = userRepository.getUser(userId)
                if (existingUser != null) {
                    emit(RegisterResult.Error("User ID already registered"))
                    return@flow
                }

                val user = User(
                    userId = userId,
                    phoneNumber = phoneNumber,
                    username = username,
                    gender = gender,
                    password = password,
                )

                val userId = userRepository.createUser(user)
                if (userId > 0) {
                    emit(RegisterResult.Success)
                } else {
                    emit(RegisterResult.Error("Registration failed"))
                }
            } catch (e: Exception) {
                emit(RegisterResult.Error(e.message ?: "Unknown error"))
            }
        }
    }.flowOn(Dispatchers.IO)
}

sealed class RegisterResult {
    object Success : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}