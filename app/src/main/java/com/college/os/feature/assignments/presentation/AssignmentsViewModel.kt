package com.college.os.feature.assignments.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.os.core.notifications.AlarmScheduler
import com.college.os.feature.assignments.data.AssignmentEntity
import com.college.os.feature.assignments.domain.AssignmentRepository
import com.college.os.feature.assignments.domain.GetAssignmentsUseCase
import com.college.os.feature.assignments.domain.ToggleAssignmentStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AssignmentsViewModel @Inject constructor(
    private val getAssignmentsUseCase: GetAssignmentsUseCase,
    private val toggleAssignmentStatusUseCase: ToggleAssignmentStatusUseCase,
    private val repository: AssignmentRepository,
    private val alarmScheduler: AlarmScheduler // 1. Inject Scheduler
) : ViewModel() {

    val assignments: StateFlow<List<AssignmentEntity>> = getAssignmentsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onToggleStatus(assignment: AssignmentEntity) {
        viewModelScope.launch {
            toggleAssignmentStatusUseCase(assignment)
        }
    }

    fun onAddAssignment(title: String, subject: String, dueDate: Long) {
        viewModelScope.launch {
            val newAssignment = AssignmentEntity(
                title = title,
                subjectName = subject,
                dueDate = dueDate
            )
            repository.insertAssignment(newAssignment)

            // 2. Schedule Reminder for 9:00 AM on Due Date
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = dueDate
            calendar.set(Calendar.HOUR_OF_DAY, 9)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            // Only schedule if the time is in the future
            if (calendar.timeInMillis > System.currentTimeMillis()) {
                alarmScheduler.scheduleOneTimeReminder(
                    id = title.hashCode(), // Use title hash as ID
                    triggerAtMillis = calendar.timeInMillis,
                    title = "Deadline: $title",
                    message = "Assignment for $subject is due today!"
                )
            }
        }
    }

    fun onDeleteAssignment(assignment: AssignmentEntity) {
        viewModelScope.launch {
            repository.deleteAssignment(assignment)
            // 3. Cleanup alarm if assignment is deleted
            alarmScheduler.cancelReminder(assignment.title.hashCode())
        }
    }
}