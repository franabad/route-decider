package com.example.routedecider

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationHelper(private val context: Context) {

    companion object {
        const val MY_CHANNEL_ID = "myChannel"
    }

    fun createChannel() {
        val channel = NotificationChannel(
            MY_CHANNEL_ID,
            "My Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "My Channel Description" }

        val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    fun createSimpleNotification(message: String) {
        val builder = NotificationCompat.Builder(context, MY_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Ruta que seguir")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }

    fun checkAndRequestNotificationPermission() {
        val permission = "android.permission.POST_NOTIFICATIONS"
        if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            (context as AppCompatActivity).requestPermissions(arrayOf(permission), 1)
        }
    }
}