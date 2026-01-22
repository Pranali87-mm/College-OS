package com.college.os.feature.notes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.os.feature.notes.data.NoteEntity
import com.college.os.feature.notes.domain.GetNotesUseCase
import com.college.os.feature.notes.domain.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val repository: NoteRepository
) : ViewModel() {

    val notes: StateFlow<List<NoteEntity>> = getNotesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onAddNote(title: String, content: String) {
        if (title.isBlank() && content.isBlank()) return

        viewModelScope.launch {
            val newNote = NoteEntity(
                title = title,
                content = content,
                timestamp = System.currentTimeMillis()
            )
            repository.insertNote(newNote)
        }
    }

    fun onDeleteNote(note: NoteEntity) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }
}