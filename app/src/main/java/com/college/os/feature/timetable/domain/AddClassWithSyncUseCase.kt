package com.college.os.feature.timetable.domain

import com.college.os.core.notifications.AlarmScheduler
import com.college.os.feature.attendance.data.AttendanceEntity
import com.college.os.feature.attendance.domain.AttendanceRepository
import com.college.os.feature.timetable.data.TimetableEntity
import javax.inject.Inject

class AddClassWithSyncUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository,
    private val attendanceRepository: AttendanceRepository,
    private val alarmScheduler: AlarmScheduler // Inject the Scheduler
) {
    suspend operator fun invoke(
        day: String,
        subject: String,
        startTime: String,
        endTime: String,
        isTheory: Boolean
    ) {
        // 1. Save to Timetable
        val newClass = TimetableEntity(
            dayOfWeek = day,
            subjectName = subject,
            startTime = startTime,
            endTime = endTime,
            isTheory = isTheory
        )
        timetableRepository.insertClass(newClass)

        // 2. Schedule Notification
        // We use the hashCode of subject+day as a unique ID for the alarm
        val alarmId = "${day}${subject}".hashCode()

        alarmScheduler.scheduleWeeklyReminder(
            id = alarmId,
            dayOfWeek = day,
            time = startTime,
            title = "Class: $subject",
            message = "Starting at $startTime. Don't be late!"
        )

        // 3. Sync with Attendance
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