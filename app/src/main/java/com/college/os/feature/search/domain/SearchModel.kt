package com.college.os.feature.search.domain

import com.college.os.feature.assignments.data.AssignmentEntity
import com.college.os.feature.notes.data.NoteEntity
import com.college.os.feature.timetable.data.TimetableEntity

sealed class SearchResult {
    data class ClassResult(val entity: TimetableEntity) : SearchResult()
    data class AssignmentResult(val entity: AssignmentEntity) : SearchResult()
    data class NoteResult(val entity: NoteEntity) : SearchResult()
}