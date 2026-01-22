package com.college.os.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.college.os.feature.attendance.data.AttendanceDao
import com.college.os.feature.attendance.data.AttendanceEntity

/**
 * The main database description.
 * We list all our Entities here. If we add "Notes" later, we add NoteEntity to the list.
 * version = 1: If we change the database structure later, we increase this number.
 */
@Database(
    entities = [AttendanceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CollegeDatabase : RoomDatabase() {

    // We expose the DAOs here so Hilt can access them.
    abstract fun attendanceDao(): AttendanceDao
}