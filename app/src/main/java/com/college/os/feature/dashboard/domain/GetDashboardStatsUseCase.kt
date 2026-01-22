package com.college.os.feature.dashboard.domain

import com.college.os.feature.assignments.domain.AssignmentRepository
import com.college.os.feature.attendance.domain.AttendanceRepository
import com.college.os.feature.timetable.domain.TimetableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetDashboardStatsUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val assignmentRepository: AssignmentRepository,
    private val timetableRepository: TimetableRepository
) {
    operator fun invoke(): Flow<DashboardStats> {
        return combine(
            attendanceRepository.getAllSubjects(),
            assignmentRepository.getAllAssignments(),
            timetableRepository.getAllClasses()
        ) { subjects, assignments, classes ->

            // 1. Calculate Attendance
            val totalAttendance = subjects.sumOf { it.currentPercentage.toDouble() }
            val averageAttendance = if (subjects.isNotEmpty()) {
                (totalAttendance / subjects.size).toFloat()
            } else {
                0f
            }

            // 2. Calculate Assignments
            val pending = assignments.count { !it.isCompleted }
            val completed = assignments.count { it.isCompleted }

            // 3. Calculate Today's Classes
            val todayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
            val classesToday = classes.count { it.dayOfWeek.equals(todayName, ignoreCase = true) }

            DashboardStats(
                overallAttendance = averageAttendance,
                totalSubjects = subjects.size,
                pendingAssignments = pending,
                completedAssignments = completed,
                classesToday = classesToday
            )
        }
    }
}