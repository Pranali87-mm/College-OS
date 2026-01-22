package com.college.os.feature.attendance.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    viewModel: AttendanceViewModel = hiltViewModel()
) {
    // 1. Collect State from ViewModel
    val subjects by viewModel.subjects.collectAsStateWithLifecycle()

    // Local state for the "Add Subject" dialog
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Subject")
            }
        }
    ) { innerPadding ->

        // 2. The List
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(subjects) { subject ->
                AttendanceItem(
                    subject = subject,
                    onPresent = { viewModel.onPresentClick(subject) },
                    onAbsent = { viewModel.onAbsentClick(subject) },
                    onDelete = { viewModel.onDeleteSubject(subject) }
                )
            }
        }

        // 3. The Dialog
        if (showDialog) {
            AddSubjectDialog(
                onDismiss = { showDialog = false },
                onConfirm = { name ->
                    viewModel.onAddSubject(name)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun AddSubjectDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Subject") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Subject Name") },
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(onClick = { if (text.isNotBlank()) onConfirm(text) }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}