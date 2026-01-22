package com.college.os.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color

// For now, we focus on the Light Theme to perfect the Minimal look.
private val LightColorScheme = lightColorScheme(
    primary = CollegePrimary,
    onPrimary = CollegeOnPrimary,
    primaryContainer = CollegePrimaryContainer,
    secondary = CollegeSecondary,
    onSecondary = CollegeOnSecondary,
    secondaryContainer = CollegeSecondaryContainer,
    tertiary = CollegeTertiary,
    background = CollegeBackground,
    surface = CollegeSurface,
    onSurface = CollegeOnSurface,
)

// We can define a Dark Scheme later, but we start with Light for clarity.
private val DarkColorScheme = darkColorScheme(
    primary = CollegePrimary,
    onPrimary = CollegeOnPrimary,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E)
)

@Composable
fun CollegeOSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is set to false to enforce our specific branding
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Set the status bar color to match the background for a seamless look
            window.statusBarColor = colorScheme.background.toArgb()
            // If light theme, use dark icons on status bar
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Uses default Material 3 typography for now
        content = content
    )
}