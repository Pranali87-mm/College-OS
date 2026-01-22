package com.college.os.feature.assignments.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.college.os.core.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentsScreen(
    viewModel: AssignmentsViewModel = hiltViewModel()
) {
    val assignments by viewModel.assignments.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assignments") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->

        if (assignments.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No pending assignments. Relax!", color = MaterialTheme.colorScheme.secondary)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(assignments) { assignment ->
                    AssignmentItem(
                        assignment = assignment,
                        onToggle = { viewModel.onToggleStatus(assignment) },
                        onDelete = { viewModel.onDeleteAssignment(assignment) }
                    )
                }
            }
        }

        if (showAddDialog) {
            AddAssignmentDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { title, subject, date ->
                    viewModel.onAddAssignment(title, subject, date)
                    showAddDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAssignmentDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Long) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }

    // Date Picker State
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate)

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { selectedDate = it }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Assignment") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Title") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject") },
                    singleLine = true
                )

                // Date Selector Button
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Due: ${DateUtils.formatDate(selectedDate)}")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && subject.isNotBlank()) {
                        onConfirm(title, subject, selectedDate)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}