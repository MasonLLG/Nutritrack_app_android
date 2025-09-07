package com.fit2081assignment3.nutritrack.viewmodel.nutricoach

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fit2081assignment3.nutritrack.data.FruitApiService
import com.fit2081assignment3.nutritrack.data.model.Fruit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutricoachViewModel @Inject constructor(
    private val apiService: FruitApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow<NutricoachUiState>(NutricoachUiState.Initial)
    val uiState: StateFlow<NutricoachUiState> = _uiState

    fun loadAllFruits() {
        viewModelScope.launch {
            _uiState.value = NutricoachUiState.Loading
            try {
                val response = apiService.getAllFruits()
                if (response.isSuccessful) {
                    _uiState.value = NutricoachUiState.Success(response.body() ?: emptyList())
                } else {
                    _uiState.value = NutricoachUiState.Error("API error: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = NutricoachUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun loadFruitById(id: Int) {
        viewModelScope.launch {
            _uiState.value = NutricoachUiState.Loading
            try {
                val response = apiService.getFruitById(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _uiState.value = NutricoachUiState.SingleFruit(it)
                    } ?: run {
                        _uiState.value = NutricoachUiState.Error("Fruit not found")
                    }
                } else {
                    _uiState.value = NutricoachUiState.Error("API error: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = NutricoachUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    var searchName = mutableStateOf("")
        private set

    fun updateSearchName(name: String) {
        searchName.value = name
    }

    fun searchFruit() {
        var inputName = searchName.value.trim()
        if (inputName.isEmpty()) {
            //_uiState.value = NutricoachUiState.Error("Please enter a valid fruit name")
            //return
            inputName = "banana"
        }

        viewModelScope.launch {
            _uiState.value = NutricoachUiState.Loading
            try {
                val response = apiService.getFruitByName(inputName)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _uiState.value = NutricoachUiState.SingleFruit(it)
                    } ?: run {
                        _uiState.value = NutricoachUiState.Error("Fruit not found")
                    }
                } else {
                    // _uiState.value = NutricoachUiState.Error("API error: ${response.code()}")
                    _uiState.value = NutricoachUiState.Error("Fruit not found")
                }
            } catch (e: Exception) {
                _uiState.value = NutricoachUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class NutricoachUiState {
    object Initial : NutricoachUiState()
    object Loading : NutricoachUiState()
    data class Success(val fruits: List<Fruit>) : NutricoachUiState()
    data class SingleFruit(val fruit: Fruit) : NutricoachUiState()
    data class Error(val message: String) : NutricoachUiState()
}