package com.college.os.feature.assignments.data

import com.college.os.feature.assignments.domain.AssignmentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AssignmentRepositoryImpl @Inject constructor(
    private val dao: AssignmentDao
) : AssignmentRepository {

    override fun getAllAssignments(): Flow<List<AssignmentEntity>> {
        return dao.getAllAssignments()
    }

    override suspend fun getAssignmentById(id: Int): AssignmentEntity? {
        return dao.getAssignmentById(id)
    }

    override suspend fun insertAssignment(assignment: AssignmentEntity) {
        dao.insertAssignment(assignment)
    }

    override suspend fun updateAssignment(assignment: AssignmentEntity) {
        dao.updateAssignment(assignment)
    }

    override suspend fun deleteAssignment(assignment: AssignmentEntity) {
        dao.deleteAssignment(assignment)
    }
}