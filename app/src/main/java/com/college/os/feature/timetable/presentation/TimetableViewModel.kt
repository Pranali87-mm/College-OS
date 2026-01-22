package com.college.os.feature.timetable.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.os.feature.timetable.data.TimetableEntity
import com.college.os.feature.timetable.domain.AddClassWithSyncUseCase
import com.college.os.feature.timetable.domain.GetWeeklyTimetableUseCase
import com.college.os.feature.timetable.domain.TimetableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val getWeeklyTimetableUseCase: GetWeeklyTimetableUseCase,
    private val addClassWithSyncUseCase: AddClassWithSyncUseCase, // Injected the new Use Case
    private val repository: TimetableRepository
) : ViewModel() {

    val timetable: StateFlow<List<TimetableEntity>> = getWeeklyTimetableUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onAddClass(
        day: String,
        subject: String,
        startTime: String,
        endTime: String,
        isTheory: Boolean
    ) {
        viewModelScope.launch {
            // Delegating to the smart Use Case to handle saving + syncing
            addClassWithSyncUseCase(day, subject, startTime, endTime, isTheory)
        }
    }

    fun onDeleteClass(timetableEntity: TimetableEntity) {
        viewModelScope.launch {
            repository.deleteClass(timetableEntity)
        }
    }
}