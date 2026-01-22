package com.college.os.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.college.os.feature.assignments.data.AssignmentDao
import com.college.os.feature.assignments.data.AssignmentEntity
import com.college.os.feature.attendance.data.AttendanceDao
import com.college.os.feature.attendance.data.AttendanceEntity

/**
 * Updated Database.
 * Version is now 2.
 * Added AssignmentEntity to entities list.
 */
@Database(
    entities = [AttendanceEntity::class, AssignmentEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CollegeDatabase : RoomDatabase() {

    abstract fun attendanceDao(): AttendanceDao

    // New DAO for assignments
    abstract fun assignmentDao(): AssignmentDao
}