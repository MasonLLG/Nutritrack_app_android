package com.fit2081assignment3.nutritrack.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fit2081assignment3.nutritrack.data.repository.AuthManager
import com.fit2081assignment3.nutritrack.data.repository.NutritionDao
import com.fit2081assignment3.nutritrack.view.dashboard.DashboardScreen
import com.fit2081assignment3.nutritrack.view.login.LoginScreen
import com.fit2081assignment3.nutritrack.view.questionnaire.QuestionnaireScreen
import com.fit2081assignment3.nutritrack.view.register.RegisterScreen
import com.fit2081assignment3.nutritrack.view.welcome.WelcomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authManager: AuthManager

    @Inject
    lateinit var nutritionDao: NutritionDao

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            val (isLoggedIn, userId) = authManager.loginState.first()
            var startDestination = "welcome"
            if (isLoggedIn && userId != null) {
                val hasData = withContext(Dispatchers.IO) {
                    nutritionDao.getByUserId(userId.toInt()) != null
                }
                startDestination = if (hasData) "dashboard" else "questionnaire"
            }
            setContent {
                val navController = rememberNavController()
                AppNavigation(navController, startDestination)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun AppNavigation(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("welcome") { WelcomeScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("questionnaire") { QuestionnaireScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
    }
}