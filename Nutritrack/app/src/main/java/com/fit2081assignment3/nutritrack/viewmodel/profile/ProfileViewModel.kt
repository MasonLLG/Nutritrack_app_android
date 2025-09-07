package com.fit2081assignment3.nutritrack.viewmodel.profile

import android.annotation.SuppressLint
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081assignment3.nutritrack.data.model.User
import com.fit2081assignment3.nutritrack.data.repository.UserRepository
import com.fit2081assignment3.nutritrack.data.repository.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authManager: AuthManager
) : ViewModel() {
    // UI status
    private val _uiState = mutableStateOf<ProfileUiState>(ProfileUiState.Loading)
    val uiState: State<ProfileUiState> = _uiState

    // Modify the status of user information
    private val _editState = mutableStateOf<EditState>(EditState.Idle)
    val editState: State<EditState> = _editState

    // Current user information for editing
    var editableUser by mutableStateOf(User(userId = "", username = "", gender = "Female", phoneNumber = "", password = ""))
        private set
    var editableUserBak by mutableStateOf(User(userId = "", username = "", gender = "Female", phoneNumber = "", password = ""))
        private set

    // Load user information
    fun loadUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = ProfileUiState.Loading
            try {
                val userId = authManager.loginState.first().second
                val user = userRepository.getUser(userId.toString())
                if (user != null) {
                    editableUser = user
                    editableUser = editableUser.copy(password = "")
                    editableUserBak = user
                    _uiState.value = ProfileUiState.Success(user)
                } else {
                    _uiState.value = ProfileUiState.Error("User not found")
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(
                    e.message ?: "Failed to load profile"
                )
            }
        }
    }

    // Update user information
    fun updateUserField(field: String, value: String) {
        editableUser = when (field) {
            "userId" -> editableUser.copy(userId = value)
            "username" -> editableUser.copy(username = value)
            "phoneNumber" -> editableUser.copy(phoneNumber = value)
            "password" -> editableUser.copy(password = value)
            else -> { return }
        }
    }

    // Save user information
    fun saveUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _editState.value = EditState.Loading
            try {
                if (editableUser.password == "") {
                    editableUser = editableUser.copy(password = editableUserBak.password)
                }
                userRepository.updateUser(editableUser)
                _editState.value = EditState.Success
                _uiState.value = ProfileUiState.Success(editableUser)
            } catch (e: Exception) {
                _editState.value = EditState.Error(
                    e.message ?: "Failed to save profile"
                )
            }
        }
    }

    @SuppressLint("NewApi")
    fun loginOut(): Flow<ProfileResult> = flow {
        try {
            authManager.clearLoginState()
            emit(ProfileResult.Success)
        } catch (e: Exception) {
            emit(ProfileResult.Error(e.message ?: "Login out failed"))
        } finally {
        }
    }.flowOn(Dispatchers.IO)
}

// UI status
sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: User) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

// Edit status
sealed class EditState {
    object Idle : EditState()
    object Loading : EditState()
    object Success : EditState()
    data class Error(val message: String) : EditState()
}

sealed class ProfileResult {
    object Success : ProfileResult()
    data class Error(val message: String) : ProfileResult()
}