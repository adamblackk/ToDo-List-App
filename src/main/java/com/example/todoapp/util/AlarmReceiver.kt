package com.example.todoapp.util

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todoapp.MainActivity
import com.example.todoapp.R

class AlarmReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val intent=Intent(context,MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK )
        val pendingIntent=PendingIntent.getActivities(context,0, arrayOf(intent),0)


        val builder=Notification.Builder(context,"alarm")
            .setSmallIcon(R.drawable.baseline_add_alarm_24)
            .setContentTitle("Todo App Reminder")
            .setContentText("Bir hatırlatıcın var")
            .setShowWhen(true)
            .setAutoCancel(false)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val  notManager= context?.let { NotificationManagerCompat.from(it) }
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notManager!!.notify(123,builder.build())

    }
}