package com.college.os.core.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Create the DataStore extension property
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
    private val USER_NAME = stringPreferencesKey("user_name")

    // Read: Has user finished setup? (Default false)
    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_ONBOARDING_COMPLETED] ?: false
        }

    // Read: User Name (Default "Student")
    val userName: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_NAME] ?: "Student"
        }

    // Write: Save completion status
    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_ONBOARDING_COMPLETED] = completed
        }
    }

    // Write: Save Name
    suspend fun setUserName(name: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
        }
    }
}