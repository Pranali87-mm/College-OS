package com.college.os.feature.dashboard.domain

data class DashboardStats(
    val overallAttendance: Float = 0f,
    val totalSubjects: Int = 0,
    val pendingAssignments: Int = 0,
    val completedAssignments: Int = 0,
    val classesToday: Int = 0
)