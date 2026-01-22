package com.college.os.feature.assignments.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignments_table")
data class AssignmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val subjectName: String,

    // We store the deadline as a Timestamp (milliseconds).
    // It's faster to sort than Strings.
    val dueDate: Long,

    val isCompleted: Boolean = false,

    // Optional: For the "AI Helper" later, we might want to flag difficult tasks
    val details: String = ""
)