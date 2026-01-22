package com.college.os.feature.timetable.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {

    // Get schedule for a specific day, ordered by time (Morning classes first)
    @Query("SELECT * FROM timetable_table WHERE dayOfWeek = :day ORDER BY startTime ASC")
    fun getClassesForDay(day: String): Flow<List<TimetableEntity>>

    // Get ALL classes (Useful for the weekly view)
    // We use triple quotes (""") to allow the SQL to span multiple lines
    @Query("""
        SELECT * FROM timetable_table ORDER BY 
        CASE 
            WHEN dayOfWeek = 'Monday' THEN 1 
            WHEN dayOfWeek = 'Tuesday' THEN 2 
            WHEN dayOfWeek = 'Wednesday' THEN 3 
            WHEN dayOfWeek = 'Thursday' THEN 4 
            WHEN dayOfWeek = 'Friday' THEN 5 
            WHEN dayOfWeek = 'Saturday' THEN 6 
            ELSE 7 
        END, startTime ASC
    """)
    fun getAllClasses(): Flow<List<TimetableEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(timetableEntry: TimetableEntity)

    @Delete
    suspend fun deleteClass(timetableEntry: TimetableEntity)
}