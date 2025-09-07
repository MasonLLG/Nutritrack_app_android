package com.fit2081assignment3.nutritrack.view.dashboard.nutricoach

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fit2081assignment3.nutritrack.data.model.Fruit
import com.fit2081assignment3.nutritrack.viewmodel.nutricoach.NutricoachUiState
import com.fit2081assignment3.nutritrack.viewmodel.nutricoach.NutricoachViewModel

@Composable
fun NutricoachScreen(
    navController: NavController,
    innerPadding: PaddingValues,
    viewModel: NutricoachViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fruit Name", fontWeight = FontWeight.Bold)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Search
            BasicTextField(
                value = viewModel.searchName.value,
                onValueChange = { viewModel.updateSearchName(it) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .height(40.dp)
                    .weight(1f)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                decorationBox = { innerTextField ->
                    Box {
                        if (viewModel.searchName.value.isEmpty()) {
                            Text(
                                text = "banana",
                                fontSize = 20.sp,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Search button
            Button(
                onClick = { viewModel.searchFruit() }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Login",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Details")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val state = uiState) {
            is NutricoachUiState.Initial -> {
                Text("Enter fruit name", color = Color.Gray)
            }
            is NutricoachUiState.Loading -> {
                CircularProgressIndicator()
            }
            is NutricoachUiState.SingleFruit -> {
                NutritionCard(fruit = state.fruit)
            }
            is NutricoachUiState.Error -> {
                Text("${state.message}", color = Color.Red)
            }
            is NutricoachUiState.Success -> TODO()
        }
    }
}

@Composable
private fun NutritionCard(fruit: Fruit) {
    Card(
        // elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(1.dp)
        ) {
            /*
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = fruit.name.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            */

            NutritionRow("family", fruit.family)
            NutritionRow("calories", "${fruit.nutritions.calories}")
            NutritionRow("fat", "${fruit.nutritions.fat}")
            NutritionRow("sugar", "${fruit.nutritions.sugar}")
            NutritionRow("carbohydrates", "${fruit.nutritions.carbohydrates}")
            NutritionRow("protein", "${fruit.nutritions.protein}")
        }
    }
}

@Composable
private fun NutritionRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .height(32.dp)
            .drawWithContent {
                drawContent()
                drawRoundRect(
                    color = Color(200, 200, 200),
                    size = size,
                    style = Stroke(width = 1.dp.toPx()),
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.width(130.dp).align(Alignment.CenterVertically).padding(start = 10.dp)
        )
        Text(
            text = ": $value",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}