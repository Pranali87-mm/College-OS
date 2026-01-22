package com.college.os.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    // Pattern: "Jan 23, 2026"
    private val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    fun formatDate(timestamp: Long): String {
        return dateFormatter.format(Date(timestamp))
    }

    // Helper to check if a date is in the past (Overdue)
    fun isOverdue(timestamp: Long): Boolean {
        val now = System.currentTimeMillis()
        // If timestamp is before now, it's overdue.
        // We give a small buffer (one day) usually, but strict for now.
        return timestamp < now
    }
}