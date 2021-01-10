package com.example.myapplication.ui.reminder

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.R

class SchedulingWorker(appContext:Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, "123123")
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(inputData.getString("Title"))
            .setContentText(inputData.getString("Description"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(123123, notification)
        return Result.success()
    }
}