package com.college.os.feature.timetable.domain

import com.college.os.feature.attendance.data.AttendanceEntity
import com.college.os.feature.attendance.domain.AttendanceRepository
import com.college.os.feature.timetable.data.TimetableEntity
import javax.inject.Inject

class AddClassWithSyncUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository,
    private val attendanceRepository: AttendanceRepository
) {
    suspend operator fun invoke(
        day: String,
        subject: String,
        startTime: String,
        endTime: String,
        isTheory: Boolean
    ) {
        // 1. Save to Timetable (The primary action)
        val newClass = TimetableEntity(
            dayOfWeek = day,
            subjectName = subject,
            startTime = startTime,
            endTime = endTime,
            isTheory = isTheory
        )
        timetableRepository.insertClass(newClass)

        // 2. Sync with Attendance (The smart background action)
        // Check if this subject already exists in the Attendance list
        val existingSubject = attendanceRepository.getSubjectByName(subject)

        if (existingSubject == null) {
            // It's a new subject! Create a tracker for it.
            attendanceRepository.insertSubject(
                AttendanceEntity(subjectName = subject)
            )
        }
    }
}