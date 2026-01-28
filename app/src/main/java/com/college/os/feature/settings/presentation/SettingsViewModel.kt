package com.college.os.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.college.os.core.data.CollegeDatabase
import com.college.os.core.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val database: CollegeDatabase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    fun resetApp() {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Wipe the Database (Timetable, Notes, etc.)
            database.clearAllTables()

            // 2. Reset Onboarding Flag
            settingsRepository.setOnboardingCompleted(false)
            settingsRepository.setUserName("Student")

            // Note: After this, the user should restart the app or we navigate them to Onboarding.
            // Since we currently have the "Emergency Bypass" in MainActivity,
            // this will just clear the data for now, which is safe.
        }
    }
}