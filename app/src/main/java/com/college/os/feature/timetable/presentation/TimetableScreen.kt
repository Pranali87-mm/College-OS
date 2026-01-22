package com.college.os.feature.timetable.presentation

import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(
    viewModel: TimetableViewModel = hiltViewModel()
) {
    val timetable by viewModel.timetable.collectAsStateWithLifecycle()

    // Days configuration
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    var selectedDayIndex by remember { mutableIntStateOf(0) }
    val selectedDay = days[selectedDayIndex]

    // Filter classes for the selected day
    val todaysClasses = timetable.filter { it.dayOfWeek == selectedDay }

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // REMOVED TopAppBar (Title) but KEPT the Tabs
            // These tabs will appear just below the Global Top Bar
            ScrollableTabRow(
                selectedTabIndex = selectedDayIndex,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                edgePadding = 0.dp
            ) {
                days.forEachIndexed { index, day ->
                    Tab(
                        selected = selectedDayIndex == index,
                        onClick = { selectedDayIndex = index },
                        text = { Text(day.take(3)) } // Show "Mon", "Tue"
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Class")
            }
        }
    ) { innerPadding ->

        if (todaysClasses.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No classes on $selectedDay. Enjoy!", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(todaysClasses) { item ->
                    TimetableItem(
                        item = item,
                        onDelete = { viewModel.onDeleteClass(item) }
                    )
                }
            }
        }

        if (showDialog) {
            AddClassDialog(
                initialDay = selectedDay,
                days = days,
                onDismiss = { showDialog = false },
                onConfirm = { day, subject, start, end, isTheory ->
                    viewModel.onAddClass(day, subject, start, end, isTheory)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AddClassDialog(
    initialDay: String,
    days: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, Boolean) -> Unit
) {
    var subject by remember { mutableStateOf("") }
    // We use the initialDay (from the active tab) as the locked day for adding

    var startTime by remember { mutableStateOf("09:00") }
    var endTime by remember { mutableStateOf("10:00") }
    var isTheory by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    fun showTimePicker(onTimeSelected: (String) -> Unit) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val formatted = String.format("%02d:%02d", hour, minute)
                onTimeSelected(formatted)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // 24 hour view
        ).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Class") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Day: $initialDay", style = MaterialTheme.typography.labelLarge)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = { showTimePicker { startTime = it } }) {
                        Text("Start: $startTime")
                    }
                    OutlinedButton(onClick = { showTimePicker { endTime = it } }) {
                        Text("End: $endTime")
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Theory", modifier = Modifier.weight(1f))
                    Switch(
                        checked = !isTheory,
                        onCheckedChange = { isTheory = !it }
                    )
                    Text("Practical", modifier = Modifier.padding(start = 8.dp))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (subject.isNotBlank()) {
                        onConfirm(initialDay, subject, startTime, endTime, isTheory)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}