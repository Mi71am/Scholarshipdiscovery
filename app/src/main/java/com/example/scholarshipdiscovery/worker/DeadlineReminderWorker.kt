package com.example.scholarshipdiscovery.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker.Result
import androidx.work.WorkerParameters
import com.example.scholarshipdiscovery.R
import com.example.scholarshipdiscovery.data.local.ScholarshipDatabase
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class DeadlineReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val db = ScholarshipDatabase.getDatabase(context)
            val savedList = db.savedScholarshipDao().getAllSavedScholarshipsList()

            if (savedList.isEmpty()) {
                return Result.success()
            }

            val today = LocalDate.now()
            val urgentTitles = mutableListOf<String>()

            for (saved in savedList) {
                val scholarship = db.scholarshipDao().getScholarshipById(saved.scholarshipId) ?: continue
                val deadlineDate = parseDeadline(scholarship.deadline) ?: continue
                val daysUntilDeadline = ChronoUnit.DAYS.between(today, deadlineDate)

                // Notify for deadlines within the next 7 days (or already on/before today)
                if (daysUntilDeadline <= 7) {
                    urgentTitles.add("${scholarship.title} (${scholarship.deadline})")
                }
            }

            if (urgentTitles.isNotEmpty()) {
                val message = if (urgentTitles.size == 1) {
                    "Upcoming deadline: ${urgentTitles.first()}"
                } else {
                    "${urgentTitles.size} saved scholarships have deadlines soon: ${urgentTitles.joinToString(", ")}"
                }

                sendNotification(
                    title = "Scholarship Deadline Alert",
                    message = message
                )
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun parseDeadline(rawDeadline: String): LocalDate? {
        return try {
            LocalDate.parse(rawDeadline.trim())
        } catch (_: Exception) {
            null
        }
    }

    private fun sendNotification(title: String, message: String) {
        val channelId = "scholarship_deadline_channel"
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
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