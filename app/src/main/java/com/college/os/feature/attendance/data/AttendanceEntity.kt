package com.college.os.feature.attendance.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single subject in the database (e.g., "Mathematics").
 * The table name will be "attendance_table".
 */
@Entity(tableName = "attendance_table")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val subjectName: String,

    val totalClasses: Int = 0,

    val attendedClasses: Int = 0,

    // We might want to "archive" a subject (like from a previous semester)
    // without deleting it.
    val isArchived: Boolean = false
) {
    // Helper property to calculate percentage instantly
    val currentPercentage: Float
        get() = if (totalClasses == 0) 0f else (attendedClasses.toFloat() / totalClasses.toFloat()) * 100
}