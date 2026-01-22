package com.college.os.feature.attendance.domain

import com.college.os.feature.attendance.data.AttendanceEntity
import javax.inject.Inject

/**
 * Use Case: Update Attendance
 * Responsibility: Handle the logic of marking a class as attended or missed.
 * It ensures data integrity (e.g., we don't accidentally set negative numbers).
 */
class UpdateAttendanceUseCase @Inject constructor(
    private val repository: AttendanceRepository
) {

    suspend fun markPresent(subject: AttendanceEntity) {
        // Logic: Increment both attended and total
        val updatedSubject = subject.copy(
            attendedClasses = subject.attendedClasses + 1,
            totalClasses = subject.totalClasses + 1
        )
        repository.updateSubject(updatedSubject)
    }

    suspend fun markAbsent(subject: AttendanceEntity) {
        // Logic: Increment ONLY total (you missed it, but class happened)
        val updatedSubject = subject.copy(
            totalClasses = subject.totalClasses + 1
        )
        repository.updateSubject(updatedSubject)
    }

    suspend fun undoAction(subject: AttendanceEntity, wasPresent: Boolean) {
        // Logic: Revert the last action if user tapped by mistake
        val newTotal = (subject.totalClasses - 1).coerceAtLeast(0)
        val newAttended = if (wasPresent) {
            (subject.attendedClasses - 1).coerceAtLeast(0)
        } else {
            subject.attendedClasses
        }

        repository.updateSubject(subject.copy(
            totalClasses = newTotal,
            attendedClasses = newAttended
        ))
    }
}