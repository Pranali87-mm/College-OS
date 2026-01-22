package com.college.os.feature.assignments.domain

import com.college.os.feature.assignments.data.AssignmentEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAssignmentsUseCase @Inject constructor(
    private val repository: AssignmentRepository
) {
    operator fun invoke(): Flow<List<AssignmentEntity>> {
        return repository.getAllAssignments()
    }
}