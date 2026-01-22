package com.college.os.feature.attendance.domain

import com.college.os.feature.attendance.data.AttendanceEntity
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {

    fun getAllSubjects(): Flow<List<AttendanceEntity>>

    suspend fun getSubjectById(id: Int): AttendanceEntity?

    // --- NEW: Check if subject exists ---
    suspend fun getSubjectByName(name: String): AttendanceEntity?

    suspend fun insertSubject(subject: AttendanceEntity)

    suspend fun updateSubject(subject: AttendanceEntity)

    suspend fun deleteSubject(subject: AttendanceEntity)
}