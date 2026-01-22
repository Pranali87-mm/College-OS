package com.college.os.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.college.os.feature.assignments.presentation.AssignmentsScreen
import com.college.os.feature.attendance.presentation.AttendanceScreen
import com.college.os.feature.notes.presentation.NotesScreen
import com.college.os.feature.planner.presentation.PlannerScreen
import com.college.os.feature.timetable.presentation.TimetableScreen
import com.college.os.feature.timer.presentation.TimerScreen
import kotlinx.coroutines.launch

// Define our App Destinations
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Planner : Screen("planner", "Daily Plan", Icons.Default.DateRange)
    object Attendance : Screen("attendance", "Attendance", Icons.Default.Home)
    object Assignments : Screen("assignments", "Assignments", Icons.Default.CheckCircle)
    object Timetable : Screen("timetable", "Timetable", Icons.Default.List)
    object Notes : Screen("notes", "Notes", Icons.Default.Edit)
    object Timer : Screen("timer", "Focus Timer", Icons.Default.Star) // Added Timer
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // List of sidebar items
    val items = listOf(
        Screen.Planner,
        Screen.Attendance,
        Screen.Assignments,
        Screen.Timetable,
        Screen.Notes,
        Screen.Timer // Added Timer
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentScreen = items.find {
        currentDestination?.hierarchy?.any { dest -> dest.route == it.route } == true
    } ?: Screen.Planner

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Sidebar Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "College OS",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Student Dashboard",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Sidebar Items
                items.forEach { screen ->
                    NavigationDrawerItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentScreen.title) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Planner.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Planner.route) {
                    PlannerScreen()
                }
                composable(Screen.Attendance.route) {
                    AttendanceScreen()
                }
                composable(Screen.Assignments.route) {
                    AssignmentsScreen()
                }
                composable(Screen.Timetable.route) {
                    TimetableScreen()
                }
                composable(Screen.Notes.route) {
                    NotesScreen()
                }
                composable(Screen.Timer.route) { // Added Timer Route
                    TimerScreen()
                }
            }
        }
    }
}