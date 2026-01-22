package com.college.os.feature.planner.domain

import com.college.os.feature.assignments.domain.AssignmentRepository
import com.college.os.feature.timetable.domain.TimetableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetDailyPlanUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository,
    private val assignmentRepository: AssignmentRepository
) {
    // We expect the selectedDate in Milliseconds
    operator fun invoke(selectedDate: Long): Flow<List<PlannerItem>> {

        // 1. Convert selected date to Day Name (e.g., "Monday") for Timetable lookup
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val selectedDayName = dayFormat.format(Date(selectedDate))

        // 2. Combine the streams
        return combine(
            timetableRepository.getAllClasses(),
            assignmentRepository.getAllAssignments()
        ) { classes, assignments ->
            val items = mutableListOf<PlannerItem>()

            // A. Filter Classes for the specific day of the week
            val todaysClasses = classes.filter { it.dayOfWeek.equals(selectedDayName, ignoreCase = true) }
            todaysClasses.forEach {
                items.add(PlannerItem.ClassSession(it))
            }

            // B. Filter Assignments due on this specific date
            // We compare day/month/year (ignoring specific time of day for simplicity)
            val selectedDateString = formatDateRaw(selectedDate)
            val todaysAssignments = assignments.filter {
                formatDateRaw(it.dueDate) == selectedDateString
            }
            todaysAssignments.forEach {
                items.add(PlannerItem.AssignmentDue(it))
            }

            // C. Sort everything by time
            // For assignments, we treat them as "All Day" or prioritize them at 8 AM for sorting
            items.sortedBy { item ->
                when (item) {
                    is PlannerItem.ClassSession -> item.entity.startTime // e.g. "09:00"
                    is PlannerItem.AssignmentDue -> "23:59" // Assignments show at bottom of day? Or top? Let's put at end.
                }
            }
        }
    }

    // Helper to compare dates without time (dd/MM/yyyy)
    private fun formatDateRaw(millis: Long): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }
}