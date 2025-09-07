package com.fit2081assignment3.nutritrack.view.dashboard.insights

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fit2081assignment3.nutritrack.viewmodel.insights.InsightsViewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun InsightsScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val user by viewModel.user.collectAsState()
    val nutrition by viewModel.nutrition.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadUser()
        viewModel.loadNutrition()
    }

    // Various score data
    val categories = listOf(
        "VegetablesHEIFAscore" to "Vegetables",
        "FruitHEIFAscore" to "Fruits",
        "GrainsandcerealsHEIFAscore" to "Grains & Cereals",
        "WholegrainsHEIFAscore" to "Whole Grains",
        "MeatandalternativesHEIFAscore" to "Meat & Alternatives",
        "DairyandalternativesHEIFAscore" to "Dairy",
        "WaterHEIFAscore" to "Water",
        "UnsaturatedFatHEIFAscore" to "Unsaturated Fats",
        "SodiumHEIFAscore" to "Sodium",
        "SugarHEIFAscore" to "Sugar",
        "AlcoholHEIFAscore" to "Alcohol",
        "DiscretionaryHEIFAscore" to "Discretionary"
    )

    // Calculate the total score
    val totalScore = when(nutrition?.sex) {
        "Male" -> nutrition!!.heifaTotalScoreMale
        "Female" -> nutrition!!.heifaTotalScoreFemale
        else -> 0.0
    }

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Insights: Food Score",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp),
        )

        // Display of scores for each item
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            categories.forEach { (scoreKey, displayName) ->
                val score = viewModel.getScoreByKey(scoreKey)
                val maxScore = if (displayName in listOf("Water", "Alcohol")) 5 else 10

                ScoreItem(
                    category = displayName,
                    score = score,
                    maxScore = maxScore
                )
            }
        }

        // Total score display
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp, start = 6.dp)
        ) {
            Text(
                text = "Total Food Quality Score:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().height(40.dp).heightIn(min = 36.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Slider(
                    value = totalScore.toFloat(),
                    onValueChange = { },
                    valueRange = 0f..100f,
                    steps = 5,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp, end = 8.dp)
                        .height(30.dp),
                    colors = SliderDefaults.colors(
                        //thumbColor = Color(0xFF4CAF50),
                        //activeTrackColor = Color(0xFF4CAF50),
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    //enabled = false // Set as read-only
                )

                val formattedScore = formatScore(totalScore.toFloat())
                Text(
                    text = "$formattedScore/100",
                    style = MaterialTheme.typography.bodyLarge,
                    //fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(100.dp),
                    //color = Color(0xFF4CAF50)
                )
            }
        }

        // Bottom button
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Button(
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Check out my nutrition score: $totalScore! " +
                                    "Download NutriTrack to track your health!"
                        )
                        type = "text/plain"
                    }

                    // Activate the sharing dialog box
                    try {
                        context.startActivity(Intent.createChooser(sendIntent, null))
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            context,
                            "No app can handle this request",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share with someone",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share with someone")
                }
            }

            Button(
                onClick = { navController.navigate("Nutricoach") }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Improve my diet!",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Improve my diet!")
                }
            }
        }
    }
}

@Composable
private fun ScoreItem(category: String, score: Float, maxScore: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().height(40.dp).heightIn(min = 36.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .width(140.dp)
                .padding(end = 3.dp)
        )

        Slider(
            value = score,
            onValueChange = { },
            valueRange = 0f..maxScore.toFloat(),
            steps = 5,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .height(30.dp),
            colors = SliderDefaults.colors(
                //thumbColor = Color(0xFF4CAF50),
                //activeTrackColor = Color(0xFF4CAF50),
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            //enabled = false // Set as read-only
        )

        val formattedScore = formatScore(score)
        Text(
            text = "$formattedScore/$maxScore",
            style = MaterialTheme.typography.bodyLarge,
            //fontWeight = FontWeight.Bold,
            modifier = Modifier.width(60.dp),
            //color = Color(0xFF4CAF50)
        )
    }
}

fun formatScore(score: Float): String {
    val df = DecimalFormat("#.##").apply {
        decimalFormatSymbols = DecimalFormatSymbols(Locale.US)
        isDecimalSeparatorAlwaysShown = false
    }
    return df.format(score)
}