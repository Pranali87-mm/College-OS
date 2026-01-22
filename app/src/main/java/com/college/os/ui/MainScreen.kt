package com.college.os.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.college.os.feature.assignments.presentation.AssignmentsScreen
import com.college.os.feature.attendance.presentation.AttendanceScreen

// Define our App Destinations
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Attendance : Screen("attendance", "Attendance", Icons.Default.Home)
    object Assignments : Screen("assignments", "Assignments", Icons.Default.DateRange)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // List of tabs
    val items = listOf(
        Screen.Attendance,
        Screen.Assignments
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // re-selecting the same item
                                launchSingleTop = true
                                // Restore state when re-selecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        // The container that swaps the screens
        NavHost(
            navController = navController,
            startDestination = Screen.Attendance.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Attendance.route) {
                AttendanceScreen()
            }
            composable(Screen.Assignments.route) {
                AssignmentsScreen()
            }
        }
    }
}