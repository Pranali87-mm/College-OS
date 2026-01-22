package com.college.os

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CollegeOsApp : Application() {
    // This class triggers Hilt's code generation.
    // We can also use it to initialize other global libraries (like Logging) later.
}