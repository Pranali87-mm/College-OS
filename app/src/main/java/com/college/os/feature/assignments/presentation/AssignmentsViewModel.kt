package com.college.os.feature.assignments.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.os.feature.assignments.data.AssignmentEntity
import com.college.os.feature.assignments.domain.AssignmentRepository
import com.college.os.feature.assignments.domain.GetAssignmentsUseCase
import com.college.os.feature.assignments.domain.ToggleAssignmentStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignmentsViewModel @Inject constructor(
    private val getAssignmentsUseCase: GetAssignmentsUseCase,
    private val toggleAssignmentStatusUseCase: ToggleAssignmentStatusUseCase,
    private val repository: AssignmentRepository
) : ViewModel() {

    // The UI observes this list
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
        }
    }

    fun onDeleteAssignment(assignment: AssignmentEntity) {
        viewModelScope.launch {
            repository.deleteAssignment(assignment)
        }
    }
}