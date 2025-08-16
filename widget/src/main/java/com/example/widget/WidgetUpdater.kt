package com.example.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

const val ACTION_UPDATE_CLOCK_WIDGET = "com.example.clock.widget.UPDATE"

fun scheduleMinuteUpdates(context: Context) {
    val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, MinuteTickReceiver::class.java).apply { action = ACTION_UPDATE_CLOCK_WIDGET }
    val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    val pi = PendingIntent.getBroadcast(context, 0, intent, flags)

    // Schedule at the next exact minute, then reschedule each onReceive
    val cal = Calendar.getInstance().apply {
        add(Calendar.MINUTE, 1)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
    } else {
        am.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
    }
}