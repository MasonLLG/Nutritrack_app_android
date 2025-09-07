package com.fit2081assignment3.nutritrack.view.dashboard.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.fit2081assignment3.nutritrack.R
import com.fit2081assignment3.nutritrack.viewmodel.home.HomeViewModel

@Composable
fun HomeScreen(
    navControllerMain: NavController,
    navController: NavHostController,
    innerPadding: PaddingValues,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val nutrition by viewModel.nutrition.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadUser()
        viewModel.loadNutrition()
    }

    Column(
        modifier = Modifier.padding(innerPadding).padding(16.dp)
    ) {
        Text(
            text = "Hello",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = user?.userId ?: "",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "You've already filled in your Food intake Questionnaire",
                modifier = Modifier.weight(1f)
            )

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = {
                    navControllerMain.navigate("questionnaire")
                },
                modifier = Modifier.wrapContentWidth()
            ) {
                Text("Edit")
            }
        }

        Image(
            painter = painterResource(id = R.drawable.love_food_sydney2),
            contentDescription = "love_food",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(bottom = 16.dp),
            contentScale = ContentScale.Crop
        )

        val totalScore = when(nutrition?.sex) {
            "Male" -> nutrition!!.heifaTotalScoreMale
            "Female" -> nutrition!!.heifaTotalScoreFemale
            else -> 0.0
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Food Quality Score",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text =  "%.2f".format(totalScore),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "What is the Food Quality Score?\n\n" +
                    "Your Food Quality Score provides a snapshot of how well your eating patterns " +
                    "align with established food guidelines, helping you identify both strengths " +
                    "and opportunities for improvement in your diet.\n\n" +
                    "This personalized measurement considers various food groups including " +
                    "vegetables, fruits, whole grains, and proteins to give you practical " +
                    "insights for making healthier food choices.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}