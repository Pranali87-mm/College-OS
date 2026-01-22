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

    @Query("SELECT * FROM attendance_table WHERE isArchived = 0 ORDER BY subjectName ASC")
    fun getAllSubjects(): Flow<List<AttendanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subject: AttendanceEntity)

    @Update
    suspend fun updateSubject(subject: AttendanceEntity)

    @Delete
    suspend fun deleteSubject(subject: AttendanceEntity)

    @Query("SELECT * FROM attendance_table WHERE id = :id")
    suspend fun getSubjectById(id: Int): AttendanceEntity?

    // --- NEW: Check if subject exists ---
    @Query("SELECT * FROM attendance_table WHERE subjectName = :name LIMIT 1")
    suspend fun getSubjectByName(name: String): AttendanceEntity?
}