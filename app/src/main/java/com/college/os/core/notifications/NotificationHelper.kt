package com.college.os.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.college.os.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        const val CHANNEL_CLASSES = "channel_classes"
        const val CHANNEL_DEADLINES = "channel_deadlines"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val classChannel = NotificationChannel(
                CHANNEL_CLASSES,
                "Class Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for upcoming classes"
            }

            val deadlineChannel = NotificationChannel(
                CHANNEL_DEADLINES,
                "Assignment Deadlines",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for assignment due dates"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannels(listOf(classChannel, deadlineChannel))
        }
    }

    fun showNotification(id: Int, title: String, message: String, isClass: Boolean) {
        // We use a safe check for permission before showing,
        // but the caller usually handles the request logic.
        try {
            val channelId = if (isClass) CHANNEL_CLASSES else CHANNEL_DEADLINES
            val icon = if (isClass) R.drawable.ic_launcher_foreground else R.drawable.ic_launcher_foreground // Replace with specific icons if you have them

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            // Note: In a real app, we check ActivityCompat.checkSelfPermission here
            NotificationManagerCompat.from(context).notify(id, builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}