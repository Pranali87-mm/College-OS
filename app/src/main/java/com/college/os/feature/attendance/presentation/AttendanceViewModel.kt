package com.college.os.feature.attendance.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.os.feature.attendance.data.AttendanceEntity
import com.college.os.feature.attendance.domain.AttendanceRepository
import com.college.os.feature.attendance.domain.GetSubjectsUseCase
import com.college.os.feature.attendance.domain.UpdateAttendanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceViewModel @Inject constructor(
    private val getSubjectsUseCase: GetSubjectsUseCase,
    private val updateAttendanceUseCase: UpdateAttendanceUseCase,
    private val repository: AttendanceRepository // Used for adding new subjects directly
) : ViewModel() {

    // The UI observes this. "stateIn" converts the Flow to a StateFlow efficiently.
    val subjects: StateFlow<List<AttendanceEntity>> = getSubjectsUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Event: User clicks "Present"
    fun onPresentClick(subject: AttendanceEntity) {
        viewModelScope.launch {
            updateAttendanceUseCase.markPresent(subject)
        }
    }

    // Event: User clicks "Absent"
    fun onAbsentClick(subject: AttendanceEntity) {
        viewModelScope.launch {
            updateAttendanceUseCase.markAbsent(subject)
        }
    }

    // Event: Add a new subject (Simple version for now)
    fun onAddSubject(name: String) {
        viewModelScope.launch {
            repository.insertSubject(AttendanceEntity(subjectName = name))
        }
    }

    // Event: Delete a subject
    fun onDeleteSubject(subject: AttendanceEntity) {
        viewModelScope.launch {
            repository.deleteSubject(subject)
        }
    }
}