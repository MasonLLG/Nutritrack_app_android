package com.fit2081assignment3.nutritrack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081assignment3.nutritrack.data.repository.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val authManager: AuthManager) : ViewModel() {
    private val _loginState = MutableStateFlow<Pair<Boolean, String?>?>(null)
    val loginState: MutableStateFlow<Pair<Boolean, String?>?> = _loginState

    init {
        viewModelScope.launch {
            authManager.loginState.collectLatest { state ->
                _loginState.value = state
            }
        }
    }
}