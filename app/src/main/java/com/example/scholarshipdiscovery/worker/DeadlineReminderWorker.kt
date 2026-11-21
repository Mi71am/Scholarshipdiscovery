package com.example.scholarshipdiscovery.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker.Result
import com.example.scholarshipdiscovery.R
import com.example.scholarshipdiscovery.data.local.ScholarshipDatabase

class DeadlineReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val db = ScholarshipDatabase.getDatabase(context)
        val savedList = db.savedScholarshipDao().getAllSavedScholarships().value ?: emptyList()

        if (savedList.isNotEmpty()) {
            sendNotification(
                "Scholarship Deadline Alert",
                "You have ${savedList.size} saved scholarship opportunities! Check deadlines today."
            )
        }

        return Result.success()
    }

    private fun sendNotification(title: String, message: String) {
        val channelId = "scholarship_deadline_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Scholarship Deadlines",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_discover)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        try {
            notificationManager.notify(1001, notification)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
