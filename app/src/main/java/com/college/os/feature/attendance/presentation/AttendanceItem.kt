package com.college.os.feature.attendance.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.college.os.feature.attendance.data.AttendanceEntity

@Composable
fun AttendanceItem(
    subject: AttendanceEntity,
    onPresent: () -> Unit,
    onAbsent: () -> Unit,
    onDelete: () -> Unit
) {
    // Dynamic Color Logic: Red if < 75%, otherwise Primary Color
    val progressColor = if (subject.currentPercentage < 75f && subject.totalClasses > 0) {
        MaterialTheme.colorScheme.tertiary // Red/Danger
    } else {
        MaterialTheme.colorScheme.secondary // Green/Sage
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Row 1: Name and Percentage
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = subject.subjectName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${subject.currentPercentage.toInt()}%",
                    style = MaterialTheme.typography.titleLarge,
                    color = progressColor,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row 2: Progress Bar
            LinearProgressIndicator(
                progress = { subject.currentPercentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Row 3: Stats and Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stats Text
                Text(
                    text = "${subject.attendedClasses} / ${subject.totalClasses} Classes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Action Buttons
                Row {
                    // Absent Button
                    IconButton(
                        onClick = onAbsent,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Absent")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Present Button
                    IconButton(
                        onClick = onPresent,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Present")
                    }
                }
            }
        }
    }
}