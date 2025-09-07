package com.fit2081assignment3.nutritrack.view.register

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fit2081assignment3.nutritrack.viewmodel.register.RegisterResult
import com.fit2081assignment3.nutritrack.viewmodel.register.RegisterViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val context = LocalContext.current
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
                text = "Register",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 30.dp)
            )

            OutlinedTextField(
                value = viewModel.userId,
                onValueChange = { newValue ->
                    val filtered = newValue.filter { it.isDigit() }
                    viewModel.updateUserId(filtered)
                },
                label = { Text("My ID (Provided by your Clinician)") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 40.dp),
                leadingIcon = {
                    Icon(Icons.Default.Face, contentDescription = "Email")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            OutlinedTextField(
                value = viewModel.username,
                onValueChange = viewModel::updateUsername,
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(0.9f).padding(top = 16.dp),
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = "Username")
                }
            )

            OutlinedTextField(
                value = viewModel.phoneNumber,
                onValueChange = viewModel::updatePhoneNumber,
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(0.9f).padding(top = 16.dp),
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = "Phone Number")
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
                visualTransformation = PasswordVisualTransformation()
            )

            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = viewModel::updateConfirmPassword,
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(0.9f).padding(top = 16.dp),
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = "Confirm Password")
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )

            // Gender
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(0.9f).padding(top = 16.dp)
            ) {
                Text(
                    text = "Gender",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                    modifier = Modifier.weight(0.2f).align(Alignment.CenterVertically)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.8f)
                ) {
                    val genders = listOf("Male", "Female")
                    genders.forEach { gender ->
                        RadioButton(
                            selected = gender == viewModel.gender,
                            onClick = { viewModel.updateGender(gender) },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Text(text = gender, fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            Text(
                text = "This app is only for pre-registered users. Please enter your ID, phone number and password to claim your account.",
                modifier = Modifier.padding(10.dp)
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
                        viewModel.register().collect { result ->
                            when (result) {
                                is RegisterResult.Success -> {
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                }

                                is RegisterResult.Error -> {
                                    errorMessage = result.message
                                    showError = true
                                }

                                else -> {}
                            }
                        }
                    }
                }
            ) {
                Text("Register", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(0.9f),
                onClick = { navController.navigate("login") { popUpTo("register") { inclusive = true }}}
            ) {
                Text("Login", fontSize = 20.sp)
            }
        }
    }
}