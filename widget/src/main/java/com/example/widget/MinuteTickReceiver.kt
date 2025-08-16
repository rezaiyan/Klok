package com.example.widget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.glance.appwidget.updateAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MinuteTickReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_UPDATE_CLOCK_WIDGET) {
            CoroutineScope(Dispatchers.Default).launch {
                AnalogClockGlanceWidget().updateAll(context)
                // Re-schedule the next tick
                scheduleMinuteUpdates(context)
            }
        }
    }
}