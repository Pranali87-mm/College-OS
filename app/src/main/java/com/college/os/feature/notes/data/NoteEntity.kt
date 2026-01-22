package com.college.os.feature.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val content: String,

    // We store this to sort notes by "Recently Modified"
    val timestamp: Long
)