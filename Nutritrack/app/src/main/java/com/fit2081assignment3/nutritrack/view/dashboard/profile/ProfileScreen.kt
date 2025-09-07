package com.fit2081assignment3.nutritrack.view.dashboard.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fit2081assignment3.nutritrack.data.model.User
import com.fit2081assignment3.nutritrack.viewmodel.profile.ProfileResult
import com.fit2081assignment3.nutritrack.viewmodel.profile.ProfileUiState
import com.fit2081assignment3.nutritrack.viewmodel.profile.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(
    navControllerMain: NavController,
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    val uiState by viewModel.uiState
    val coroutineScope = rememberCoroutineScope()

    when (uiState) {
        is ProfileUiState.Loading -> LoadingScreen()
        is ProfileUiState.Success -> ProfileContent(
            navControllerMain = navControllerMain,
            navController = navController,
            viewModel = viewModel,
            coroutineScope = coroutineScope,
            user = (uiState as ProfileUiState.Success).user,
        )
        is ProfileUiState.Error -> ErrorScreen(
            message = (uiState as ProfileUiState.Error).message,
            onRetry = {}
        )
        else -> {}
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Error: ${message}",
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRetry) {
                Text("Reload")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ProfileContent(
    navControllerMain: NavController,
    navController: NavController,
    viewModel: ProfileViewModel,
    coroutineScope: CoroutineScope,
    user: User,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AccountInformation(navController, viewModel, coroutineScope, user)
        Divider(
            color = Color(200,200,200),
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth(0.95f).padding(top = 16.dp)
        )
        OtherItems(navControllerMain, navController, viewModel, coroutineScope, user);
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AccountInformation(
    navController: NavController,
    viewModel: ProfileViewModel,
    coroutineScope: CoroutineScope,
    user: User
) {
    val items = listOf(
        user.username to Icons.Default.Person,
        user.phoneNumber to Icons.Default.Phone,
        user.userId to Icons.Default.DateRange,
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        shape = MaterialTheme.shapes.medium,
        // elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ACCOUNT",
                color = Color(200, 200, 200),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(16.dp).padding(bottom = 0.dp).weight(1f)
            )
            Row(
                modifier = Modifier.padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EditProfile(navController, viewModel, coroutineScope)
            }
        }
        // Spacer(modifier = Modifier.height(8.dp))

        items.forEach { (title, icon) ->
            AccountCard(title = title, icon = icon)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AccountCard(title: String, icon: ImageVector) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { },
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun OtherItems(
    navControllerMain: NavController,
    navController: NavController,
    viewModel: ProfileViewModel,
    coroutineScope: CoroutineScope,
    user: User
) {
    data class OtherItem(
        val title: String,
        val icon: ImageVector,
        val onClick: () -> Unit
    )

    val items = listOf(
        OtherItem(
            "Logout",
            Icons.Default.ExitToApp,
            onClick = { logout(navControllerMain, viewModel, coroutineScope) }
        ),
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        shape = MaterialTheme.shapes.medium,
        // elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = "OTHER SETTINGS",
            color = Color(200, 200, 200),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(16.dp).padding(bottom = 0.dp)
        )

        items.forEach { (title, icon, onclick) ->
            OtherCard(title = title, icon = icon, onclick = onclick)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun OtherCard(title: String, icon: ImageVector, onclick: () -> Unit) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { onclick() },
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

fun logout(
    navController: NavController,
    viewModel: ProfileViewModel,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        viewModel.loginOut().collect { result ->
            when (result) {
                is ProfileResult.Success -> {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }

                is ProfileResult.Error -> {
                }

                else -> {}
            }
        }
    }
}