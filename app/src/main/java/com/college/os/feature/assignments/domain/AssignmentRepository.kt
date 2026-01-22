package com.college.os.feature.assignments.domain

import com.college.os.feature.assignments.data.AssignmentEntity
import kotlinx.coroutines.flow.Flow

interface AssignmentRepository {
    fun getAllAssignments(): Flow<List<AssignmentEntity>>

    suspend fun getAssignmentById(id: Int): AssignmentEntity?

    suspend fun insertAssignment(assignment: AssignmentEntity)

    suspend fun updateAssignment(assignment: AssignmentEntity)

    suspend fun deleteAssignment(assignment: AssignmentEntity)
}