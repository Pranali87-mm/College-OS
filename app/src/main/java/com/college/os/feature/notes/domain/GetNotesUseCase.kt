package com.college.os.feature.notes.domain

import com.college.os.feature.notes.data.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<NoteEntity>> {
        return repository.getAllNotes()
    }
}