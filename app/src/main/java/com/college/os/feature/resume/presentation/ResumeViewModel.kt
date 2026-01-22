package com.college.os.feature.resume.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ResumeViewModel @Inject constructor() : ViewModel() {

    // Form State
    var name = MutableStateFlow("")
    var email = MutableStateFlow("")
    var phone = MutableStateFlow("")
    var education = MutableStateFlow("")
    var skills = MutableStateFlow("")
    var experience = MutableStateFlow("")

    fun updateName(value: String) { name.value = value }
    fun updateEmail(value: String) { email.value = value }
    fun updatePhone(value: String) { phone.value = value }
    fun updateEducation(value: String) { education.value = value }
    fun updateSkills(value: String) { skills.value = value }
    fun updateExperience(value: String) { experience.value = value }
}