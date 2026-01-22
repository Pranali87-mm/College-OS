package com.college.os.feature.notes.data

import com.college.os.feature.notes.domain.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<NoteEntity>> {
        return dao.getAllNotes()
    }

    override suspend fun getNoteById(id: Int): NoteEntity? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: NoteEntity) {
        dao.insertNote(note)
    }

    override suspend fun updateNote(note: NoteEntity) {
        dao.updateNote(note)
    }

    override suspend fun deleteNote(note: NoteEntity) {
        dao.deleteNote(note)
    }
}