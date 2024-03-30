package com.example.weatherapplication.alarm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.weatherapplication.MainActivity
import com.example.weatherapplication.R
import com.example.weatherapplication.channelID
import com.example.weatherapplication.messageExtra
import com.example.weatherapplication.notificationID
import com.example.weatherapplication.titleExtra


class Notification : BroadcastReceiver() {

    // Method called when the broadcast is received
    override fun onReceive(context: Context, intent: Intent) {

        val i = Intent(context, MainActivity::class.java)

        // Create a copy of the intent and set flags on the copy
        val newIntent = i.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, newIntent,
            PendingIntent.FLAG_IMMUTABLE)

        // Build the notification using NotificationCompat.Builder
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_alert)
            .setContentTitle(intent.getStringExtra(titleExtra)) // Set title from intent
            .setContentText(intent.getStringExtra(messageExtra))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // Get the NotificationManager service
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Show the notification using the manager
        manager.notify(notificationID, notification)
    }
}