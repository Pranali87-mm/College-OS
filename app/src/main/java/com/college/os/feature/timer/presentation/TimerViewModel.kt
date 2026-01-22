package com.college.os.feature.timer.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel() {

    // --- Configuration State ---
    private val _focusDurationMinutes = MutableStateFlow(25)
    val focusDurationMinutes: StateFlow<Int> = _focusDurationMinutes.asStateFlow()

    private val _breakDurationMinutes = MutableStateFlow(5)
    val breakDurationMinutes: StateFlow<Int> = _breakDurationMinutes.asStateFlow()

    private val _targetSessions = MutableStateFlow(4)
    val targetSessions: StateFlow<Int> = _targetSessions.asStateFlow()

    // --- Runtime State ---
    private val _timeRemaining = MutableStateFlow(25 * 60L)
    val timeRemaining: StateFlow<Long> = _timeRemaining.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private val _progress = MutableStateFlow(1.0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    private val _isFocusPhase = MutableStateFlow(true) // True = Focus, False = Break
    val isFocusPhase: StateFlow<Boolean> = _isFocusPhase.asStateFlow()

    private val _currentSession = MutableStateFlow(1)
    val currentSession: StateFlow<Int> = _currentSession.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    private var timerJob: Job? = null
    private var totalTimeForPhase = 25 * 60L

    // --- Settings Updates ---
    fun updateSettings(focusMin: Int, breakMin: Int, sessions: Int) {
        _focusDurationMinutes.value = focusMin
        _breakDurationMinutes.value = breakMin
        _targetSessions.value = sessions
        resetTimer() // Apply changes immediately
    }

    fun toggleTimer() {
        if (_isRunning.value) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        _isRunning.value = true
        _isFinished.value = false

        timerJob = viewModelScope.launch {
            while (_currentSession.value <= _targetSessions.value) {

                // Run the countdown for the current phase
                while (_timeRemaining.value > 0 && _isRunning.value) {
                    delay(1000L)
                    _timeRemaining.value -= 1
                    _progress.value = _timeRemaining.value.toFloat() / totalTimeForPhase.toFloat()
                }

                // If paused manually, break the loop
                if (!_isRunning.value) break

                // Phase Finished! Switch Mode.
                if (_isFocusPhase.value) {
                    // Focus ended -> Start Break
                    _isFocusPhase.value = false
                    totalTimeForPhase = _breakDurationMinutes.value * 60L
                    _timeRemaining.value = totalTimeForPhase
                    // TODO: Trigger notification sound here
                } else {
                    // Break ended -> Start Next Focus Session
                    _isFocusPhase.value = true
                    _currentSession.value += 1

                    if (_currentSession.value > _targetSessions.value) {
                        // All sessions done!
                        finishTimer()
                        break
                    }

                    totalTimeForPhase = _focusDurationMinutes.value * 60L
                    _timeRemaining.value = totalTimeForPhase
                    // TODO: Trigger notification sound here
                }

                // Reset progress visually for the new phase
                _progress.value = 1.0f
            }
        }
    }

    private fun pauseTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    private fun finishTimer() {
        _isRunning.value = false
        _isFinished.value = true
        _progress.value = 0f
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _currentSession.value = 1
        _isFocusPhase.value = true
        _isFinished.value = false

        // Reset based on current settings
        totalTimeForPhase = _focusDurationMinutes.value * 60L
        _timeRemaining.value = totalTimeForPhase
        _progress.value = 1.0f
    }

    fun formatTime(seconds: Long): String {
        val min = seconds / 60
        val sec = seconds % 60
        return String.format("%02d:%02d", min, sec)
    }
}