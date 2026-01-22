package com.college.os.feature.timetable.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.os.feature.timetable.data.TimetableEntity
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
    private val repository: TimetableRepository
) : ViewModel() {

    // The full schedule
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
            val newClass = TimetableEntity(
                dayOfWeek = day,
                subjectName = subject,
                startTime = startTime,
                endTime = endTime,
                isTheory = isTheory
            )
            repository.insertClass(newClass)
        }
    }

    fun onDeleteClass(timetableEntity: TimetableEntity) {
        viewModelScope.launch {
            repository.deleteClass(timetableEntity)
        }
    }
}