package com.fit2081assignment3.nutritrack.view.questionnaire

import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fit2081assignment3.nutritrack.viewmodel.questionnaire.Persona
import com.fit2081assignment3.nutritrack.viewmodel.questionnaire.QuestionnaireViewModel

@Composable
fun QuestionnaireScreen(
    navController: NavController? = null,
    viewModel: QuestionnaireViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadQuestionnaireData()
    }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Food Categories Section
            Text(
                "Tick all the food categories you can eat:",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Checkboxes Grid
            Column(Modifier.fillMaxWidth()) {
                // Row 1
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FoodCheckbox(
                        index = 0,
                        label = "Fruits",
                        checked = uiState.checkedStates[0],
                        onCheckedChange = viewModel::updateCheckState
                    )
                    FoodCheckbox(
                        index = 1,
                        label = "Vegetables",
                        checked = uiState.checkedStates[1],
                        onCheckedChange = viewModel::updateCheckState
                    )
                    FoodCheckbox(
                        index = 2,
                        label = "Grains",
                        checked = uiState.checkedStates[2],
                        onCheckedChange = viewModel::updateCheckState
                    )
                }

                // Row 2
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FoodCheckbox(
                        index = 3,
                        label = "Red Meat",
                        checked = uiState.checkedStates[3],
                        onCheckedChange = viewModel::updateCheckState
                    )
                    FoodCheckbox(
                        index = 4,
                        label = "Seafood",
                        checked = uiState.checkedStates[4],
                        onCheckedChange = viewModel::updateCheckState
                    )
                    FoodCheckbox(
                        index = 5,
                        label = "Poultry",
                        checked = uiState.checkedStates[5],
                        onCheckedChange = viewModel::updateCheckState
                    )
                }

                // Row 3
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FoodCheckbox(
                        index = 6,
                        label = "Fish",
                        checked = uiState.checkedStates[6],
                        onCheckedChange = viewModel::updateCheckState
                    )
                    FoodCheckbox(
                        index = 7,
                        label = "Eggs",
                        checked = uiState.checkedStates[7],
                        onCheckedChange = viewModel::updateCheckState
                    )
                    FoodCheckbox(
                        index = 8,
                        label = "Nuts/Seeds",
                        checked = uiState.checkedStates[8],
                        onCheckedChange = viewModel::updateCheckState
                    )
                }
            }
        }

        item {
            // Persona Section
            PersonaSection(
                viewModel = viewModel,
                selectedPersona = uiState.selectedPersona,
                onPersonaSelected = viewModel::updateSelectedPersona
            )
        }

        item {
            // Time Selection Section
            TimeSelectionSection(
                mealTime = uiState.mealTime,
                sleepTime = uiState.sleepTime,
                wakeTime = uiState.wakeTime,
                onMealTimeChange = viewModel::updateMealTime,
                onSleepTimeChange = viewModel::updateSleepTime,
                onWakeTimeChange = viewModel::updateWakeTime
            )
        }

        item {
            ActionButtons(
                onLoad = { viewModel.loadQuestionnaireData() },
                onSave = {
                    viewModel.saveQuestionnaireData()
                    navController?.navigate("dashboard")
                }
            )
        }
    }
}

@Composable
private fun FoodCheckbox(
    index: Int,
    label: String,
    checked: Boolean,
    onCheckedChange: (Int, Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(100.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheckedChange(index, it) }
        )
        Text(text = label)
    }
}

@Composable
private fun PersonaSection(
    viewModel: QuestionnaireViewModel,
    selectedPersona: Persona?,
    onPersonaSelected: (Persona) -> Unit
) {
    Column {
        Text(
            "Your Persona",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "People can be broadly classified into 6 different types based on their " +
                    "eating preferences. Click on each button below to find out " +
                    "the different types, and select the type that best fits you!"
        )

        Spacer(Modifier.height(8.dp))

        PersonaSelectionDialog(viewModel.getPersonas(), onPersonaSelected = onPersonaSelected)

        Text(
            "Which persona best fits you?",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        PersonaDropdownSelector(
            personas = viewModel.getPersonas(),
            selectedPersona = selectedPersona,
            onPersonaSelected = onPersonaSelected,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TimeSelectionSection(
    mealTime: String,
    sleepTime: String,
    wakeTime: String,
    onMealTimeChange: (String) -> Unit,
    onSleepTimeChange: (String) -> Unit,
    onWakeTimeChange: (String) -> Unit
) {
    Column {
        Text(
            "Timings",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        TimeInputRow(
            label = "Biggest meal time",
            selectedTime = mealTime,
            onTimeChange = onMealTimeChange
        )
        TimeInputRow(
            label = "Sleep time",
            selectedTime = sleepTime,
            onTimeChange = onSleepTimeChange
        )
        TimeInputRow(
            label = "Wake up time",
            selectedTime = wakeTime,
            onTimeChange = onWakeTimeChange
        )
    }
}

@Composable
private fun ActionButtons(onLoad: () -> Unit, onSave: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onLoad,
            modifier = Modifier.weight(1f)
        ) {
            Text("Reset Saved")
        }

        Spacer(Modifier.width(16.dp))

        Button(
            onClick = onSave,
            modifier = Modifier.weight(1f)
        ) {
            Text("Save Questionnaire")
        }
    }
}

@Composable
fun PersonaSelectionDialog(
    personas: List<Persona>,
    onPersonaSelected: (Persona) -> Unit
) {
    var showDialogForPersona by remember { mutableStateOf<Persona?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        personas.forEach { persona ->
            Button(
                onClick = { showDialogForPersona = persona },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(persona.title)
            }
        }
    }

    showDialogForPersona?.let { persona ->
        AlertDialog(
            onDismissRequest = { showDialogForPersona = null },
            title = {
                Text(
                    text = persona.title,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Image(
                        painter = painterResource(id = persona.imageResId),
                        contentDescription = "Persona Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(bottom = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = persona.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onPersonaSelected(persona)
                        showDialogForPersona = null
                    }
                ) { Text("Select") }
            },
            dismissButton = {
                TextButton(onClick = { showDialogForPersona = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun TimeInputRow(
    label: String,
    selectedTime: String,
    onTimeChange: (String) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Processing time format
    val initialTime = remember {
        val parts = selectedTime.split(":")
        if (parts.size == 2) {
            Pair(parts[0].toIntOrNull() ?: 0, parts[1].toIntOrNull() ?: 0)
        } else {
            Pair(0, 0)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Surface(
            modifier = Modifier
                .width(120.dp)
                .clickable { showTimePicker = true },
            shape = MaterialTheme.shapes.small,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select time"
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = selectedTime,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            context = context,
            initialHour = initialTime.first,
            initialMinute = initialTime.second,
            onTimeSet = { hour, minute ->
                val formattedTime = "%02d:%02d".format(hour, minute)
                onTimeChange(formattedTime)
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

@Composable
private fun TimePickerDialog(
    context: Context,
    initialHour: Int,
    initialMinute: Int,
    onTimeSet: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePicker = android.app.TimePickerDialog(
        context,
        { _, hour, minute -> onTimeSet(hour, minute) },
        initialHour,
        initialMinute,
        true // Use 24-hour clock
    ).apply {
        setTitle("Select Time")
        setButton(DialogInterface.BUTTON_POSITIVE, "OK") { _, _ -> }
        setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel") { _, _ -> onDismiss() }
    }

    DisposableEffect(Unit) {
        timePicker.show()
        onDispose { timePicker.dismiss() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonaDropdownSelector(
    personas: List<Persona>,
    selectedPersona: Persona?,
    onPersonaSelected: (Persona) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedPersona?.title ?: "Select your persona",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null
                )
            },
            /*colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )*/
        )

        // Drop-down menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .heightIn(max = 240.dp)
        ) {
            if (personas.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No personas available") },
                    onClick = { /* 无操作 */ }
                )
            } else {
                personas.forEach { persona ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = persona.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        onClick = {
                            onPersonaSelected(persona)
                            expanded = false
                            Toast.makeText(
                                context,
                                "${persona.title} selected",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        leadingIcon = {
                            if (persona == selectedPersona) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}