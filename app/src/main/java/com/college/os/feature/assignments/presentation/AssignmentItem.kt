package com.college.os.feature.assignments.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.college.os.core.util.DateUtils
import com.college.os.feature.assignments.data.AssignmentEntity

@Composable
fun AssignmentItem(
    assignment: AssignmentEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    val isOverdue = !assignment.isCompleted && DateUtils.isOverdue(assignment.dueDate)
    val dateColor = if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Checkbox
            Checkbox(
                checked = assignment.isCompleted,
                onCheckedChange = { onToggle() }
            )

            Spacer(modifier = Modifier.width(8.dp))

            // 2. Text Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = assignment.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (assignment.isCompleted) TextDecoration.LineThrough else null
                )
                Text(
                    text = "${assignment.subjectName} • ${DateUtils.formatDate(assignment.dueDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = dateColor
                )
            }

            // 3. Delete Button (Optional, maybe hidden in a menu later)
            // For MVP, we can swipe to delete or just have a button if needed.
            // Let's keep it clean for now and NOT show a delete button to reduce clutter,
            // but the callback exists if we need it.
        }
    }
}