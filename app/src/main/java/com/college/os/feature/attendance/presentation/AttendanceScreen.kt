package com.college.os.feature.attendance.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

    // Note: We removed the local "Add" state because subjects will now come from the Timetable.

    Scaffold(
        // No TopAppBar (Global handles it)
        // No FloatingActionButton (Sync handles it)
    ) { innerPadding ->

        if (subjects.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add classes in Timetable to start tracking.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        } else {
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
        }
    }
}