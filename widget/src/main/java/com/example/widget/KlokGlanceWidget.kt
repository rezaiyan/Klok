package com.example.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent

class KlokGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
            }

        }
    }
}

class KlokWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: KlokGlanceWidget = KlokGlanceWidget()
}