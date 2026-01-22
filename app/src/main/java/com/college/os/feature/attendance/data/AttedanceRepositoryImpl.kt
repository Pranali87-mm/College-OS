package com.college.os.feature.attendance.data

import com.college.os.feature.attendance.domain.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * The Worker.
 * This lives in the "Data" layer. It implements the contract using the DAO.
 * @Inject constructor: Tells Hilt "I need the Dao to work, please give it to me."
 */
class AttendanceRepositoryImpl @Inject constructor(
    private val dao: AttendanceDao
) : AttendanceRepository {

    override fun getAllSubjects(): Flow<List<AttendanceEntity>> {
        return dao.getAllSubjects()
    }

    override suspend fun getSubjectById(id: Int): AttendanceEntity? {
        return dao.getSubjectById(id)
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