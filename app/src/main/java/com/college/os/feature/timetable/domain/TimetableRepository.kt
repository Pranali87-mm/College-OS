package com.college.os.feature.timetable.domain

import com.college.os.feature.timetable.data.TimetableEntity
import kotlinx.coroutines.flow.Flow

interface TimetableRepository {
    // For the "Today's Schedule" view
    fun getClassesForDay(day: String): Flow<List<TimetableEntity>>

    // For the "Edit Timetable" view (shows whole week)
    fun getAllClasses(): Flow<List<TimetableEntity>>

    suspend fun insertClass(timetableEntry: TimetableEntity)

    suspend fun deleteClass(timetableEntry: TimetableEntity)
}