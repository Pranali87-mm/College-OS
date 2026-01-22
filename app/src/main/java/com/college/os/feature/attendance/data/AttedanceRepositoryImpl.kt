package com.college.os.feature.attendance.data

import com.college.os.feature.attendance.domain.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val dao: AttendanceDao
) : AttendanceRepository {

    override fun getAllSubjects(): Flow<List<AttendanceEntity>> {
        return dao.getAllSubjects()
    }

    override suspend fun getSubjectById(id: Int): AttendanceEntity? {
        return dao.getSubjectById(id)
    }

    // --- NEW: Implementation ---
    override suspend fun getSubjectByName(name: String): AttendanceEntity? {
        return dao.getSubjectByName(name)
    }

    override suspend fun insertSubject(subject: AttendanceEntity) {
        dao.insertSubject(subject)
    }

    override suspend fun updateSubject(subject: AttendanceEntity) {
        dao.updateSubject(subject)
    }

    override suspend fun deleteSubject(subject: AttendanceEntity) {
        dao.deleteSubject(subject)
    }
}