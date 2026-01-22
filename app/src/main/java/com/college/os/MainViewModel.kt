package com.college.os

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.os.core.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    settingsRepository: SettingsRepository
) : ViewModel() {

    // We use a Boolean? (nullable).
    // null = Loading (Don't show anything yet)
    // false = New User (Show Onboarding)
    // true = Returning User (Show Dashboard)
    val startDestination: StateFlow<Boolean?> = settingsRepository.isOnboardingCompleted
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
}