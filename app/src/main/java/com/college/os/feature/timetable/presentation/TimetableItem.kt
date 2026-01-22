package com.college.os.feature.timetable.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.college.os.feature.timetable.data.TimetableEntity

@Composable
fun TimetableItem(
    item: TimetableEntity,
    onDelete: () -> Unit
) {
    // Visual cue for Practical vs Theory
    val cardColor = if (item.isTheory) {
        MaterialTheme.colorScheme.surface
    } else {
        MaterialTheme.colorScheme.secondaryContainer // Slight Green tint for Labs
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Time and Type
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${item.startTime} - ${item.endTime}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                SuggestionChip(
                    onClick = { },
                    label = { Text(if (item.isTheory) "Theory" else "Practical") },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (item.isTheory) Color.Transparent else MaterialTheme.colorScheme.secondary
                    )
                )
            }

            // Middle: Subject Name
            Text(
                text = item.subjectName,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(2f)
            )

            // Right: Delete Action
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Class",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}