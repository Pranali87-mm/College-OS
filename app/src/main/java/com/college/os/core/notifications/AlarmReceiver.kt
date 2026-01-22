package com.college.os.core.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("TITLE") ?: "College OS"
        val message = intent.getStringExtra("MESSAGE") ?: "Reminder"
        val isClass = intent.getBooleanExtra("IS_CLASS", true)
        val id = intent.getIntExtra("ID", 0)

        notificationHelper.showNotification(id, title, message, isClass)
    }
}