package com.college.os.core.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    // --- EXISTING: For Weekly Classes ---
    fun scheduleWeeklyReminder(
        id: Int,
        dayOfWeek: String,
        time: String,
        title: String,
        message: String
    ) {
        val (hour, minute) = time.split(":").map { it.toInt() }
        val dayInt = parseDayOfWeek(dayOfWeek)

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayInt)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 7)
        }

        schedule(id, calendar.timeInMillis, title, message, isClass = true)
    }

    // --- NEW: For One-Time Assignments ---
    fun scheduleOneTimeReminder(
        id: Int,
        triggerAtMillis: Long,
        title: String,
        message: String
    ) {
        schedule(id, triggerAtMillis, title, message, isClass = false)
    }

    // --- Helper to reduce code duplication ---
    private fun schedule(id: Int, timeInMillis: Long, title: String, message: String, isClass: Boolean) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("ID", id)
            putExtra("TITLE", title)
            putExtra("MESSAGE", message)
            putExtra("IS_CLASS", isClass)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                timeInMillis,
                pendingIntent
            )
            Log.d("AlarmScheduler", "Scheduled $title for $timeInMillis")
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    fun cancelReminder(id: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun parseDayOfWeek(day: String): Int {
        return when (day.lowercase(Locale.getDefault())) {
            "sunday" -> Calendar.SUNDAY
            "monday" -> Calendar.MONDAY
            "tuesday" -> Calendar.TUESDAY
            "wednesday" -> Calendar.WEDNESDAY
            "thursday" -> Calendar.THURSDAY
            "friday" -> Calendar.FRIDAY
            "saturday" -> Calendar.SATURDAY
            else -> Calendar.MONDAY
        }
    }
}