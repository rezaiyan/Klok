package com.example.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.glance.LocalSize
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import java.time.LocalTime
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import androidx.core.graphics.createBitmap

class AnalogClockGlanceWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val size = LocalSize.current
            val px = run {
                val density = context.resources.displayMetrics.density
                // Reduce the size to account for padding
                val paddedSize = min(size.width.value, size.height.value) - 16 // 8dp padding on each side
                (paddedSize * density).roundToInt().coerceAtLeast(64)
            }
            val bmp = renderAnalogClockBitmap(px, LocalTime.now())

            Box(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .clickable(actionRunCallback<RefreshAction>())
            ) {
                Image(
                    provider = ImageProvider(bmp),
                    contentDescription = "Analog Clock",
                    modifier = GlanceModifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

class AnalogClockGlanceReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = AnalogClockGlanceWidget()
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        AnalogClockGlanceWidget().update(context, glanceId)
    }
}

// Reusable renderer for Glance (Android Canvas -> Bitmap)
fun renderAnalogClockBitmap(sizePx: Int, time: LocalTime): Bitmap {
    val bmp = createBitmap(sizePx, sizePx)
    val c = Canvas(bmp)

    val cx = sizePx / 2f
    val cy = sizePx / 2f
    val r = sizePx / 2f * 0.94f

    val face = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFFF8F8F8.toInt() }
    val rim = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFE0E0E0.toInt(); style = Paint.Style.STROKE; strokeWidth = r * 0.03f
    }
    val tickMajor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF333333.toInt(); strokeCap = Paint.Cap.ROUND; strokeWidth = r * 0.014f
    }
    val tickMinor = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF888888.toInt(); strokeCap = Paint.Cap.ROUND; strokeWidth = r * 0.006f
    }
    val hourPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF111111.toInt(); strokeCap = Paint.Cap.ROUND; strokeWidth = r * 0.04f
    }
    val minutePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF222222.toInt(); strokeCap = Paint.Cap.ROUND; strokeWidth = r * 0.03f
    }
    val secondPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFD32F2F.toInt(); strokeCap = Paint.Cap.ROUND; strokeWidth = r * 0.012f
    }
    val centerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF111111.toInt() }

    // Face & rim
    c.drawCircle(cx, cy, r, face)
    c.drawCircle(cx, cy, r, rim)

    // Ticks
    for (i in 0 until 60) {
        val isMajor = i % 5 == 0
        val tickLen = if (isMajor) r * 0.12f else r * 0.06f
        val angle = Math.toRadians(i * 6.0 - 90.0)
        val sx = cx + cos(angle).toFloat() * (r - tickLen)
        val sy = cy + sin(angle).toFloat() * (r - tickLen)
        val ex = cx + cos(angle).toFloat() * r
        val ey = cy + sin(angle).toFloat() * r
        c.drawLine(sx, sy, ex, ey, if (isMajor) tickMajor else tickMinor)
    }

    val hour = time.hour % 12
    val minute = time.minute
    val second = time.second

    val hourAngle = ((hour) + minute / 60f + second / 3600f) * 30f - 90f
    val minuteAngle = (minute + second / 60f) * 6f - 90f
    val secondAngle = second * 6f - 90f

    fun hand(angleDeg: Float, length: Float, widthScale: Float, p: Paint, tail: Float = 0f) {
        p.strokeWidth = r * widthScale
        val rad = Math.toRadians(angleDeg.toDouble())
        val ex = cx + cos(rad).toFloat() * (r * length)
        val ey = cy + sin(rad).toFloat() * (r * length)
        val tx = cx - cos(rad).toFloat() * (r * tail)
        val ty = cy - sin(rad).toFloat() * (r * tail)
        c.drawLine(tx, ty, ex, ey, p)
    }

    hand(hourAngle, 0.55f, 0.04f, hourPaint, tail = 0.08f)
    hand(minuteAngle, 0.75f, 0.03f, minutePaint, tail = 0.10f)
    // hand(secondAngle, 0.85f, 0.012f, secondPaint, tail = 0.15f)

    c.drawCircle(cx, cy, r * 0.03f, centerPaint)
    return bmp
}
