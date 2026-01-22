package com.college.os.feature.attendance.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    // Get all subjects that are NOT archived, ordered by name.
    // Return Flow<> so the UI updates automatically when data changes.
    @Query("SELECT * FROM attendance_table WHERE isArchived = 0 ORDER BY subjectName ASC")
    fun getAllSubjects(): Flow<List<AttendanceEntity>>

    // Insert a new subject. If ID conflicts, replace it.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: AttendanceEntity)

    // Update an existing subject (e.g., incrementing attendance).
    @Update
    suspend fun updateSubject(subject: AttendanceEntity)

    // Delete a subject permanently.
    @Delete
    suspend fun deleteSubject(subject: AttendanceEntity)

    // Get a specific subject by ID (useful for the Detail screen later).
    @Query("SELECT * FROM attendance_table WHERE id = :id")
    suspend fun getSubjectById(id: Int): AttendanceEntity?
}