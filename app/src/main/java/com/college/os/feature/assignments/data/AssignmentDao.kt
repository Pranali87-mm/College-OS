package com.college.os.feature.assignments.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignmentDao {

    // Get all tasks. Logic: Show PENDING first. Inside pending, show SOONEST deadline first.
    @Query("SELECT * FROM assignments_table ORDER BY isCompleted ASC, dueDate ASC")
    fun getAllAssignments(): Flow<List<AssignmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: AssignmentEntity)

    @Update
    suspend fun updateAssignment(assignment: AssignmentEntity)

    @Delete
    suspend fun deleteAssignment(assignment: AssignmentEntity)

    @Query("SELECT * FROM assignments_table WHERE id = :id")
    suspend fun getAssignmentById(id: Int): AssignmentEntity?
}