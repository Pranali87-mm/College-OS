package com.college.os.feature.search.domain

import com.college.os.feature.assignments.domain.AssignmentRepository
import com.college.os.feature.notes.domain.NoteRepository
import com.college.os.feature.timetable.domain.TimetableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class SearchEverywhereUseCase @Inject constructor(
    private val timetableRepository: TimetableRepository,
    private val assignmentRepository: AssignmentRepository,
    private val noteRepository: NoteRepository
) {
    operator fun invoke(query: String): Flow<List<SearchResult>> {
        // If query is empty, return nothing
        if (query.isBlank()) {
            return kotlinx.coroutines.flow.flowOf(emptyList())
        }

        val lowerQuery = query.lowercase().trim()

        return combine(
            timetableRepository.getAllClasses(),
            assignmentRepository.getAllAssignments(),
            noteRepository.getAllNotes()
        ) { classes, assignments, notes ->
            val results = mutableListOf<SearchResult>()

            // 1. Search Classes (Match Subject)
            classes.filter {
                it.subjectName.lowercase().contains(lowerQuery)
            }.forEach {
                results.add(SearchResult.ClassResult(it))
            }

            // 2. Search Assignments (Match Title or Subject)
            assignments.filter {
                it.title.lowercase().contains(lowerQuery) ||
                        it.subjectName.lowercase().contains(lowerQuery)
            }.forEach {
                results.add(SearchResult.AssignmentResult(it))
            }

            // 3. Search Notes (Match Title or Content)
            notes.filter {
                it.title.lowercase().contains(lowerQuery) ||
                        it.content.lowercase().contains(lowerQuery)
            }.forEach {
                results.add(SearchResult.NoteResult(it))
            }

            results
        }
    }
}