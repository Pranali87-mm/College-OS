package com.college.os.feature.timer.presentation

import android.media.RingtoneManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = hiltViewModel()
) {
    // Collect State
    val timeRemaining by viewModel.timeRemaining.collectAsStateWithLifecycle()
    val isRunning by viewModel.isRunning.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()
    val isFocusPhase by viewModel.isFocusPhase.collectAsStateWithLifecycle()
    val isSessionActive by viewModel.isSessionActive.collectAsStateWithLifecycle()
    val currentSession by viewModel.currentSession.collectAsStateWithLifecycle()
    val targetSessions by viewModel.targetSessions.collectAsStateWithLifecycle()

    // Slider Values
    val focusMinutes by viewModel.focusDurationMinutes.collectAsStateWithLifecycle()
    val breakMinutes by viewModel.breakDurationMinutes.collectAsStateWithLifecycle()

    val animatedProgress by animateFloatAsState(targetValue = progress, label = "Progress")
    val primaryColor = if (isFocusPhase) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary

    // --- SOUND LOGIC ---
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.timerSoundEvent.collect {
            try {
                val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(context, notification)
                r.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    // -------------------

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // --- 1. DIRECT SETTINGS (Visible only when stopped) ---
        AnimatedVisibility(
            visible = !isSessionActive,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "SESSION SETUP",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )

                    // Focus Slider Row
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Focus Duration")
                            Text(
                                "$focusMinutes min",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Slider(
                            value = focusMinutes.toFloat(),
                            onValueChange = { viewModel.onFocusTimeChanged(it.toInt()) },
                            valueRange = 5f..60f,
                            steps = 10
                        )
                    }

                    // Break Slider Row
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Break Duration")
                            Text(
                                "$breakMinutes min",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        Slider(
                            value = breakMinutes.toFloat(),
                            onValueChange = { viewModel.onBreakTimeChanged(it.toInt()) },
                            valueRange = 1f..30f,
                            steps = 28
                        )
                    }

                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                    // Sessions Counter Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total Sessions")

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilledTonalIconButton(
                                onClick = { if (targetSessions > 1) viewModel.onSessionsChanged(targetSessions - 1) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("-", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }

                            Text(
                                text = "$targetSessions",
                                modifier = Modifier.padding(horizontal = 16.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            FilledTonalIconButton(
                                onClick = { if (targetSessions < 10) viewModel.onSessionsChanged(targetSessions + 1) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // --- 2. TIMER CIRCLE ---
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
                    text = if (isFocusPhase) "Focus ($currentSession/$targetSessions)" else "Break Time",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // --- 3. BUTTONS (Equal Size) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Reset Button
            Button(
                onClick = { viewModel.resetTimer() },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp), // Fixed height matching Start
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("RESET")
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Start/Pause Button
            Button(
                onClick = { viewModel.toggleTimer() },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) MaterialTheme.colorScheme.error else primaryColor
                )
            ) {
                if (isRunning) {
                    Text("PAUSE")
                } else {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isSessionActive) "RESUME" else "START")
                }
            }
        }
    }
}