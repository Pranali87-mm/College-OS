package com.college.os.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = CollegePrimary,
    onPrimary = CollegeOnPrimary,
    primaryContainer = CollegePrimaryContainer,
    onPrimaryContainer = CollegeOnPrimaryContainer,
    secondary = CollegeSecondary,
    onSecondary = CollegeOnSecondary,
    secondaryContainer = CollegeSecondaryContainer,
    onSecondaryContainer = CollegeOnSecondaryContainer,
    tertiary = CollegeTertiary,
    background = CollegeBackground,
    surface = CollegeSurface,
    onSurface = CollegeOnSurface,
)

private val DarkColorScheme = darkColorScheme(
    primary = CollegePrimaryDark,
    onPrimary = CollegeOnPrimaryDark,
    primaryContainer = CollegePrimaryContainerDark,
    onPrimaryContainer = CollegeOnPrimary.copy(alpha = 0.9f), // Keep text readable
    secondary = CollegeSecondaryDark,
    onSecondary = CollegeOnSecondaryDark,
    secondaryContainer = CollegeSecondaryContainerDark,
    background = CollegeBackgroundDark,
    surface = CollegeSurfaceDark,
    onSurface = CollegeOnSurfaceDark,
    onSurfaceVariant = Color(0xFFC4C6D0) // Lighter variant for subtitles in dark mode
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
            window.statusBarColor = colorScheme.background.toArgb()

            // In Dark Mode, we want light icons (so isAppearanceLightStatusBars = false)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}