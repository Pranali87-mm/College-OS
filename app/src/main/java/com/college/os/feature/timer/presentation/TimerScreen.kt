package com.college.os.feature.timer.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel()
) {
    val timeRemaining by viewModel.timeRemaining.collectAsStateWithLifecycle()
    val isRunning by viewModel.isRunning.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()
    val isFocusPhase by viewModel.isFocusPhase.collectAsStateWithLifecycle()
    val currentSession by viewModel.currentSession.collectAsStateWithLifecycle()
    val targetSessions by viewModel.targetSessions.collectAsStateWithLifecycle()
    val isFinished by viewModel.isFinished.collectAsStateWithLifecycle()

    // Settings State
    val focusMinutes by viewModel.focusDurationMinutes.collectAsStateWithLifecycle()
    val breakMinutes by viewModel.breakDurationMinutes.collectAsStateWithLifecycle()
    val targetSessionCount by viewModel.targetSessions.collectAsStateWithLifecycle()
    var showSettings by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(targetValue = progress, label = "Progress")

    // Define colors based on phase
    val primaryColor = if (isFocusPhase) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary
    val containerColor = if (isFocusPhase) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiaryContainer

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSettings = true },
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Session Indicator Pill
            Surface(
                color = containerColor,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text(
                    text = if (isFinished) "All Done! 🎉"
                    else if (isFocusPhase) "Focus Session $currentSession of $targetSessions"
                    else "Break Time!",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // The Big Timer Circle
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.size(260.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    strokeWidth = 16.dp
                )

                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.size(260.dp),
                    color = primaryColor,
                    strokeWidth = 16.dp,
                    strokeCap = StrokeCap.Round
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = viewModel.formatTime(timeRemaining),
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isFocusPhase) "Stay Focused" else "Relax",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(56.dp))

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalIconButton(
                    onClick = { viewModel.resetTimer() },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset")
                }

                Button(
                    onClick = { viewModel.toggleTimer() },
                    modifier = Modifier.size(90.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRunning) MaterialTheme.colorScheme.error else primaryColor
                    )
                ) {
                    if (isRunning) {
                        Text("||", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Start", modifier = Modifier.size(32.dp))
                    }
                }
            }
        }

        // Settings Dialog
        if (showSettings) {
            TimerSettingsDialog(
                currentFocus = focusMinutes,
                currentBreak = breakMinutes,
                currentSessions = targetSessionCount,
                onDismiss = { showSettings = false },
                onConfirm = { f, b, s ->
                    viewModel.updateSettings(f, b, s)
                    showSettings = false
                }
            )
        }
    }
}

@Composable
fun TimerSettingsDialog(
    currentFocus: Int,
    currentBreak: Int,
    currentSessions: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, Int) -> Unit
) {
    var focusTime by remember { mutableIntStateOf(currentFocus) }
    var breakTime by remember { mutableIntStateOf(currentBreak) }
    var sessions by remember { mutableIntStateOf(currentSessions) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Timer Settings") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Focus Time Slider
                Column {
                    Text("Focus Time: $focusTime min")
                    Slider(
                        value = focusTime.toFloat(),
                        onValueChange = { focusTime = it.toInt() },
                        valueRange = 5f..60f,
                        steps = 10
                    )
                }

                // Break Time Slider
                Column {
                    Text("Break Time: $breakTime min")
                    Slider(
                        value = breakTime.toFloat(),
                        onValueChange = { breakTime = it.toInt() },
                        valueRange = 1f..30f,
                        steps = 28
                    )
                }

                // Sessions Counter
                Column {
                    Text("Sessions: $sessions")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(onClick = { if (sessions > 1) sessions-- }) { Text("-") }
                        Text("$sessions", fontWeight = FontWeight.Bold)
                        OutlinedButton(onClick = { if (sessions < 10) sessions++ }) { Text("+") }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(focusTime, breakTime, sessions) }) {
                Text("Save & Reset")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}