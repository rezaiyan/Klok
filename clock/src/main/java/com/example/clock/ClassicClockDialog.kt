package com.example.clock

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


@Composable
fun ClassicClock(
    modifier: Modifier = Modifier
) {
    val colorSchema = MaterialTheme.colorScheme
    // Tick every second
    val timeMillis by produceState(System.currentTimeMillis()) {
        while (true) {
            value = System.currentTimeMillis()
            delay(1000)
        }
    }

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = min(size.width, size.height) / 2f - 20.dp.toPx()

        // Face
        drawCircle(
            color = colorSchema.surfaceVariant,
            radius = radius,
            center = center
        )

        // Border (fixed style usage)
        drawCircle(
            color = colorSchema.outline,
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // Hour markers
        for (hour in 0..11) {
            val angleDeg = hour * 30f - 90f
            val startRadius = radius - 15.dp.toPx()
            val endRadius = radius - 5.dp.toPx()
            val rad = Math.toRadians(angleDeg.toDouble())
            val start = Offset(
                x = center.x + startRadius * cos(rad).toFloat(),
                y = center.y + startRadius * sin(rad).toFloat()
            )
            val end = Offset(
                x = center.x + endRadius * cos(rad).toFloat(),
                y = center.y + endRadius * sin(rad).toFloat()
            )
            drawLine(
                color = colorSchema.onSurfaceVariant,
                start = start,
                end = end,
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }

        // Time components
        val calendar = java.util.Calendar.getInstance().apply { timeInMillis = timeMillis }
        val seconds = calendar.get(java.util.Calendar.SECOND)
        val minutes = calendar.get(java.util.Calendar.MINUTE)
        val hours = calendar.get(java.util.Calendar.HOUR_OF_DAY) % 12

        // Angles
        val secondAngle = seconds * 6f - 90f
        val minuteAngle = (minutes + seconds / 60f) * 6f - 90f
        val hourAngle = (hours + minutes / 60f + seconds / 3600f) * 30f - 90f

        // Hour hand
        drawLine(
            color = colorSchema.onSurface,
            start = center,
            end = Offset(
                center.x + (radius * 0.5f) * cos(Math.toRadians(hourAngle.toDouble())).toFloat(),
                center.y + (radius * 0.5f) * sin(Math.toRadians(hourAngle.toDouble())).toFloat()
            ),
            strokeWidth = 6.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Minute hand
        drawLine(
            color = colorSchema.onSurface,
            start = center,
            end = Offset(
                center.x + (radius * 0.7f) * cos(Math.toRadians(minuteAngle.toDouble())).toFloat(),
                center.y + (radius * 0.7f) * sin(Math.toRadians(minuteAngle.toDouble())).toFloat()
            ),
            strokeWidth = 4.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Second hand
        drawLine(
            color = colorSchema.primary,
            start = center,
            end = Offset(
                center.x + (radius * 0.8f) * cos(Math.toRadians(secondAngle.toDouble())).toFloat(),
                center.y + (radius * 0.8f) * sin(Math.toRadians(secondAngle.toDouble())).toFloat()
            ),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Center dot
        drawCircle(
            color = colorSchema.primary,
            radius = 6.dp.toPx(),
            center = center
        )
    }
}
