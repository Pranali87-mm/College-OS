package com.college.os.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.college.os.feature.assignments.data.AssignmentDao
import com.college.os.feature.assignments.data.AssignmentEntity
import com.college.os.feature.attendance.data.AttendanceDao
import com.college.os.feature.attendance.data.AttendanceEntity
import com.college.os.feature.timetable.data.TimetableDao
import com.college.os.feature.timetable.data.TimetableEntity

/**
 * Version 3: Added Timetable Module
 */
@Database(
    entities = [
        AttendanceEntity::class,
        AssignmentEntity::class,
        TimetableEntity::class // Added this
    ],
    version = 3, // Bumped version
    exportSchema = false
)
abstract class CollegeDatabase : RoomDatabase() {

    abstract fun attendanceDao(): AttendanceDao
    abstract fun assignmentDao(): AssignmentDao
    abstract fun timetableDao(): TimetableDao // Added this
}