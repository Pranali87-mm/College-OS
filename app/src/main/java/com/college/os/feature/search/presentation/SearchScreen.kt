package com.college.os.feature.search.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.college.os.feature.search.domain.SearchResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val results by viewModel.results.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            // Special Search Bar Header
            TopAppBar(
                title = {
                    TextField(
                        value = query,
                        onValueChange = { viewModel.onQueryChanged(it) },
                        placeholder = { Text("Search everywhere...") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChanged("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->

        if (query.isEmpty()) {
            // Empty State (Initial)
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Find classes, tasks, or notes", color = MaterialTheme.colorScheme.secondary)
                }
            }
        } else if (results.isEmpty()) {
            // Empty State (No Results)
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No results found for '$query'", color = MaterialTheme.colorScheme.secondary)
            }
        } else {
            // Results List
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(results) { result ->
                    when (result) {
                        is SearchResult.ClassResult -> SearchClassItem(result)
                        is SearchResult.AssignmentResult -> SearchAssignmentItem(result)
                        is SearchResult.NoteResult -> SearchNoteItem(result)
                    }
                }
            }
        }
    }
}

// --- Visual Items ---

@Composable
fun SearchClassItem(result: SearchResult.ClassResult) {
    SearchResultCard(
        icon = Icons.Default.List,
        title = result.entity.subjectName,
        subtitle = "${result.entity.dayOfWeek} • ${result.entity.startTime}",
        tag = "Class"
    )
}

@Composable
fun SearchAssignmentItem(result: SearchResult.AssignmentResult) {
    SearchResultCard(
        icon = Icons.Default.DateRange,
        title = result.entity.title,
        subtitle = "Subject: ${result.entity.subjectName}",
        tag = "Task",
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun SearchNoteItem(result: SearchResult.NoteResult) {
    SearchResultCard(
        icon = Icons.Default.Edit,
        title = result.entity.title,
        subtitle = result.entity.content, // Show snippet of content
        tag = "Note",
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
fun SearchResultCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    tag: String,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = tag.uppercase(), style = MaterialTheme.typography.labelSmall, color = color)
        }
    }
}