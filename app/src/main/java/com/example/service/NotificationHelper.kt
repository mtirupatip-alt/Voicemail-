package com.example.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity

object NotificationHelper {
    const val CHANNEL_CALL_SCREENING = "channel_receptionist_screening"
    const val CHANNEL_SYSTEM_ALERTS = "channel_receptionist_alerts"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val screeningChannel = NotificationChannel(
                CHANNEL_CALL_SCREENING,
                "Screened Incoming Calls",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Shows notifications when incoming calls are processed by Offline AI Receptionist"
            }

            val alertsChannel = NotificationChannel(
                CHANNEL_SYSTEM_ALERTS,
                "Receptionist Service Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Important alerts about role permissions and screening service status"
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(screeningChannel)
            manager.createNotificationChannel(alertsChannel)
        }
    }

    fun showCallScreenedNotification(context: Context, callerDisplay: String, actionTaken: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_CALL_SCREENING)
            .setSmallIcon(android.R.drawable.stat_notify_call_mute)
            .setContentTitle("Screened Call: $callerDisplay")
            .setContentText("Status: $actionTaken • Ringing normally")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)
        } catch (_: SecurityException) {
            // Handled when POST_NOTIFICATIONS is not yet granted
        }
    }
}
