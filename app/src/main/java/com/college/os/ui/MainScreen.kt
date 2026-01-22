package com.college.os.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.college.os.core.settings.SettingsRepository
import com.college.os.feature.assignments.presentation.AssignmentsScreen
import com.college.os.feature.attendance.presentation.AttendanceScreen
import com.college.os.feature.dashboard.presentation.DashboardScreen
import com.college.os.feature.notes.presentation.NotesScreen
import com.college.os.feature.planner.presentation.PlannerScreen
import com.college.os.feature.search.presentation.SearchScreen
import com.college.os.feature.timetable.presentation.TimetableScreen
import com.college.os.feature.timer.presentation.TimerScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// Define our App Destinations
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Planner : Screen("planner", "Daily Plan", Icons.Default.DateRange)
    object Dashboard : Screen("dashboard", "Analytics", Icons.Default.Info)
    object Attendance : Screen("attendance", "Attendance", Icons.Default.Home)
    object Assignments : Screen("assignments", "Assignments", Icons.Default.CheckCircle)
    object Timetable : Screen("timetable", "Timetable", Icons.Default.List)
    object Notes : Screen("notes", "Notes", Icons.Default.Edit)
    object Timer : Screen("timer", "Focus Timer", Icons.Default.Star)
    object Search : Screen("search", "Search", Icons.Default.Search)
}

// --- NEW: ViewModel to fetch the name ---
@HiltViewModel
class MainScreenViewModel @Inject constructor(
    settingsRepository: SettingsRepository
) : ViewModel() {
    val userName = settingsRepository.userName
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainScreenViewModel = hiltViewModel() // Inject the ViewModel
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // --- NEW: Collect the User Name ---
    val userName by viewModel.userName.collectAsStateWithLifecycle(initialValue = "Student")

    // Permission Logic
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { }
    )
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val items = listOf(
        Screen.Planner,
        Screen.Dashboard,
        Screen.Attendance,
        Screen.Assignments,
        Screen.Timetable,
        Screen.Notes,
        Screen.Timer
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentScreen = items.find {
        currentDestination?.hierarchy?.any { dest -> dest.route == it.route } == true
    } ?: Screen.Planner

    val isSearchScreen = currentDestination?.route == Screen.Search.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = !isSearchScreen,
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
                        // --- UPDATED: Use dynamic name ---
                        Text(
                            text = "Hello, $userName",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

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
                if (!isSearchScreen) {
                    TopAppBar(
                        title = { Text(currentScreen.title) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                            navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Planner.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Planner.route) { PlannerScreen() }
                composable(Screen.Dashboard.route) { DashboardScreen() }
                composable(Screen.Attendance.route) { AttendanceScreen() }
                composable(Screen.Assignments.route) { AssignmentsScreen() }
                composable(Screen.Timetable.route) { TimetableScreen() }
                composable(Screen.Notes.route) { NotesScreen() }
                composable(Screen.Timer.route) { TimerScreen() }
                composable(Screen.Search.route) {
                    SearchScreen(onBack = { navController.popBackStack() })
                }
            }
        }
    }
}