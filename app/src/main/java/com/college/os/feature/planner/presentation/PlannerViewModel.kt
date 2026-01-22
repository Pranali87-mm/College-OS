package com.college.os.feature.planner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.os.feature.planner.domain.GetDailyPlanUseCase
import com.college.os.feature.planner.domain.PlannerItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class PlannerViewModel @Inject constructor(
    private val getDailyPlanUseCase: GetDailyPlanUseCase
) : ViewModel() {

    // 1. State: currently selected date (Default to Today)
    private val _selectedDate = MutableStateFlow(System.currentTimeMillis())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    // 2. Reactively fetch data whenever the date changes
    @OptIn(ExperimentalCoroutinesApi::class)
    val plannerItems: StateFlow<List<PlannerItem>> = _selectedDate
        .flatMapLatest { date ->
            getDailyPlanUseCase(date)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onDateSelected(date: Long) {
        _selectedDate.value = date
    }
}