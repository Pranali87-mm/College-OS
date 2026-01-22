package com.college.os.feature.resume.presentation

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.college.os.core.pdf.PdfGenerator
import com.college.os.core.pdf.ResumeData

@Composable
fun ResumeScreen(
    viewModel: ResumeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // State Collection
    val name by viewModel.name.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val phone by viewModel.phone.collectAsStateWithLifecycle()
    val education by viewModel.education.collectAsStateWithLifecycle()
    val skills by viewModel.skills.collectAsStateWithLifecycle()
    val experience by viewModel.experience.collectAsStateWithLifecycle()

    // File Saver Launcher
    val savePdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        uri?.let {
            // Write PDF to the selected URI
            try {
                context.contentResolver.openOutputStream(it)?.use { outputStream ->
                    val data = ResumeData(name, email, phone, education, experience, skills)
                    PdfGenerator(context).generatePdf(data, outputStream)
                    Toast.makeText(context, "Resume Saved Successfully!", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error saving PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Resume Builder",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text("Fill in your details below and hit Export.", color = MaterialTheme.colorScheme.secondary)

            // --- Personal Info ---
            Text("Personal Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.updateEmail(it) },
                    label = { Text("Email") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { viewModel.updatePhone(it) },
                    label = { Text("Phone") },
                    modifier = Modifier.weight(1f)
                )
            }

            // --- Education ---
            Text("Education", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = education,
                onValueChange = { viewModel.updateEducation(it) },
                label = { Text("Degree, College, Year") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            // --- Skills ---
            Text("Skills", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = skills,
                onValueChange = { viewModel.updateSkills(it) },
                label = { Text("List your skills (comma separated)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                maxLines = 4
            )

            // --- Experience ---
            Text("Experience / Projects", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = experience,
                onValueChange = { viewModel.updateExperience(it) },
                label = { Text("Internships, Projects, Descriptions") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 6
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Export Button
            Button(
                onClick = {
                    // Launch the system file picker to create a file
                    savePdfLauncher.launch("${name.trim()}_Resume.pdf")
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Export PDF")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}