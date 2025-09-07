package com.fit2081assignment3.nutritrack.view.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fit2081assignment3.nutritrack.viewmodel.login.LoginResult
import com.fit2081assignment3.nutritrack.viewmodel.login.LoginViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Log in",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp)
            )

            OutlinedTextField(
                value = viewModel.userId,
                onValueChange = viewModel::updateUserId,
                label = { Text("My ID (Provided by your Clinician)") },
                modifier = Modifier.fillMaxWidth(0.9f).padding(top = 40.dp),
                leadingIcon = {
                    Icon(Icons.Default.Face, contentDescription = "id")
                }
            )

            OutlinedTextField(
                value = viewModel.password,
                onValueChange = viewModel::updatePassword,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(0.9f).padding(top = 16.dp),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Password")
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
            )

            Text(
                text = "This app is only for pre-registered users. Please enter your ID and password or Register to claim your account on your first visit.",
                modifier = Modifier.padding(16.dp)
            )

            if (showError) {
                Text(errorMessage, color = Color.Red, modifier = Modifier.padding(10.dp))
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(bottom = 80.dp),
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(0.9f),
                onClick = {
                    coroutineScope.launch {
                        viewModel.login().collect { result ->
                            when (result) {
                                is LoginResult.Success -> {
                                    val hasData = viewModel.hasNutritionData(viewModel.userId)
                                    val route = if (hasData) "dashboard" else "questionnaire"
                                    navController.navigate(route) {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }

                                is LoginResult.Error -> {
                                    errorMessage = result.message
                                    showError = true
                                }

                                else -> {}
                            }
                        }
                    }
                }

            ) {
                Text("Continue", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(0.9f),
                onClick = { navController.navigate("register") { popUpTo("login") { inclusive = true }}}
            ) {
                Text("Register", fontSize = 20.sp)
            }
        }
    }
}