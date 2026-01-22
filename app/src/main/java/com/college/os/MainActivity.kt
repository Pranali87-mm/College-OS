package com.college.os

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.college.os.feature.onboarding.presentation.OnboardingScreen
import com.college.os.ui.MainScreen
import com.college.os.ui.theme.CollegeOSTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Get the MainViewModel to check user status
            val viewModel = hiltViewModel<MainViewModel>()
            val isCompleted by viewModel.startDestination.collectAsStateWithLifecycle()

            CollegeOSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (isCompleted) {
                        null -> {
                            // 1. Loading State (Show Spinner while reading DataStore)
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        false -> {
                            // 2. New User -> Show Onboarding
                            OnboardingScreen()
                        }
                        true -> {
                            // 3. Returning User -> Show Main App
                            MainScreen()
                        }
                    }
                }
            }
        }
    }
}