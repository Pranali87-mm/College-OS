package com.college.os.feature.onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.os.core.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    fun completeOnboarding(name: String) {
        viewModelScope.launch {
            // 1. Save the name (default to "Student" if empty)
            val finalName = if (name.isBlank()) "Student" else name
            settingsRepository.setUserName(finalName)

            // 2. Mark as completed so we never see this screen again
            settingsRepository.setOnboardingCompleted(true)
        }
    }
}