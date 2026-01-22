package com.college.os.feature.assignments.domain

import com.college.os.feature.assignments.data.AssignmentEntity
import javax.inject.Inject

class ToggleAssignmentStatusUseCase @Inject constructor(
    private val repository: AssignmentRepository
) {
    suspend operator fun invoke(assignment: AssignmentEntity) {
        // Create a copy of the assignment with the boolean flipped
        val updatedAssignment = assignment.copy(isCompleted = !assignment.isCompleted)
        repository.updateAssignment(updatedAssignment)
    }
}