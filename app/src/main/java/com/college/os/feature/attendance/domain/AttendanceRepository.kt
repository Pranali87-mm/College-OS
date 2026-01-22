package com.college.os.feature.attendance.domain

import com.college.os.feature.attendance.data.AttendanceEntity
import kotlinx.coroutines.flow.Flow

/**
 * The Contract.
 * This lives in the "Domain" layer. It defines what functionality we expose to the UI.
 * It does NOT know about Room or SQL.
 */
interface AttendanceRepository {

    // Returns a stream of data. If the DB changes, this updates automatically.
    fun getAllSubjects(): Flow<List<AttendanceEntity>>

    suspend fun getSubjectById(id: Int): AttendanceEntity?

    suspend fun insertSubject(subject: AttendanceEntity)

    suspend fun updateSubject(subject: AttendanceEntity)

    suspend fun deleteSubject(subject: AttendanceEntity)
}