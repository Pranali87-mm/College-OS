package com.college.os.feature.planner.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.college.os.feature.planner.domain.PlannerItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun PlannerScreen(
    viewModel: PlannerViewModel = hiltViewModel()
) {
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val plannerItems by viewModel.plannerItems.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            // 1. The Horizontal Calendar Strip
            WeekCalendarStrip(
                selectedDate = selectedDate,
                onDateSelected = { viewModel.onDateSelected(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. The Title for the section
            Text(
                text = "Schedule & Tasks",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. The Content List
            if (plannerItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No classes or tasks for this day.\nTime to relax! \uD83C\uDF89", // 🎉
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp) // Space for FAB
                ) {
                    items(plannerItems) { item ->
                        when (item) {
                            is PlannerItem.ClassSession -> PlannerClassItem(item)
                            is PlannerItem.AssignmentDue -> PlannerAssignmentItem(item)
                        }
                    }
                }
            }
        }
    }
}

// --- Components ---

@Composable
fun WeekCalendarStrip(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit
) {
    // Generate a list of dates: Today - 2 days ... Today + 10 days
    val dates = remember {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -2) // Start 2 days ago
        val list = mutableListOf<Date>()
        repeat(14) { // Generate 2 weeks
            list.add(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    // Formatters
    val dayNameFormat = SimpleDateFormat("EEE", Locale.getDefault()) // "Mon"
    val dayNumberFormat = SimpleDateFormat("dd", Locale.getDefault()) // "23"

    // Helper to check if dates match (ignoring time)
    fun isSameDay(d1: Long, d2: Long): Boolean {
        val f = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return f.format(d1) == f.format(d2)
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(dates) { date ->
            val isSelected = isSameDay(date.time, selectedDate)

            // Visual Item
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .clickable { onDateSelected(date.time) }
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = dayNameFormat.format(date).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dayNumberFormat.format(date),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun PlannerClassItem(item: PlannerItem.ClassSession) {
    val entity = item.entity
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time Column
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = entity.startTime,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = entity.endTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Vertical Divider line
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (entity.isTheory) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.secondary
                    )
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Details
            Column {
                Text(
                    text = entity.subjectName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (entity.isTheory) "Theory Class" else "Practical / Lab",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun PlannerAssignmentItem(item: PlannerItem.AssignmentDue) {
    val entity = item.entity
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f) // Reddish tint for urgency
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Deadline: ${entity.title}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Text(
                    text = entity.subjectName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}