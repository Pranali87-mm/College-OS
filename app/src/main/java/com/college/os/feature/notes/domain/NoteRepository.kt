package com.college.os.feature.notes.domain

import com.college.os.feature.notes.data.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<NoteEntity>>

    suspend fun getNoteById(id: Int): NoteEntity?

    suspend fun insertNote(note: NoteEntity)

    suspend fun updateNote(note: NoteEntity)

    suspend fun deleteNote(note: NoteEntity)
}