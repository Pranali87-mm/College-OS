package com.college.os.feature.timetable.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timetable_table")
data class TimetableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val dayOfWeek: String, // e.g., "Monday", "Tuesday"

    val subjectName: String,

    // We use 24-hour format string for sorting (e.g., "14:00")
    // It's easier to query "Order by startTime" this way.
    val startTime: String,

    val endTime: String,   // e.g., "14:45"

    // True = Theory, False = Practical
    val isTheory: Boolean = true
)