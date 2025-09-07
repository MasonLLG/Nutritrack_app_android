package com.fit2081assignment3.nutritrack.view.dashboard.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fit2081assignment3.nutritrack.viewmodel.profile.ProfileViewModel
import kotlinx.coroutines.CoroutineScope

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditProfile(
    navController: NavController,
    viewModel: ProfileViewModel,
    coroutineScope: CoroutineScope
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(28.dp)
            .background(Color(200, 230, 250, 255), RoundedCornerShape(50)),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { showEditProfileDialog = true },
            modifier = Modifier.size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    if (showEditProfileDialog) {
        EditProfileDialog(viewModel = viewModel, closeDialog = {
            showEditProfileDialog = false
        })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun EditProfileDialog(viewModel: ProfileViewModel, closeDialog: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Edit Profile") },
        text = {
            Column {

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = viewModel.editableUser.username,
                    onValueChange = { viewModel.updateUserField("username", it) },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = viewModel.editableUser.phoneNumber,
                    onValueChange ={ viewModel.updateUserField("phoneNumber", it) },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = viewModel.editableUser.password,
                    onValueChange = { viewModel.updateUserField("password", it) },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation(),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.saveUserProfile()
                    closeDialog()
                },
                enabled = true
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { closeDialog() }
            ) {
                Text("Cancel")
            }
        }
    )
}