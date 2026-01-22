package com.college.os.feature.attendance.domain

import com.college.os.feature.attendance.data.AttendanceEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use Case: Get All Subjects
 * Responsibility: Fetch the data and potentially sort or filter it before showing it.
 */
class GetSubjectsUseCase @Inject constructor(
    private val repository: AttendanceRepository
) {
    // operator fun invoke allows us to call this class like a function: getSubjectsUseCase()
    operator fun invoke(): Flow<List<AttendanceEntity>> {
        return repository.getAllSubjects()
    }
}