package com.example.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sin
import java.time.LocalTime
import kotlinx.coroutines.delay

@Immutable
data class ClockColors(
    val face: Color = Color(0xFFF8F8F8),
    val tickMajor: Color = Color(0xFF333333),
    val tickMinor: Color = Color(0xFF888888),
    val hourHand: Color = Color(0xFF111111),
    val minuteHand: Color = Color(0xFF222222),
    val secondHand: Color = Color(0xFFD32F2F),
    val center: Color = Color(0xFF111111),
    val rim: Color = Color(0xFFE0E0E0)
)

@Composable
fun rememberClockTime(tickMillis: Long = 1000L): State<LocalTime> {
    val time = remember { mutableStateOf(LocalTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            time.value = LocalTime.now()
            delay(tickMillis)
        }
    }
    return time
}

@Composable
fun AnalogClock(
    modifier: Modifier = Modifier,
    colors: ClockColors = ClockColors(),
    showSeconds: Boolean = true,
    time: LocalTime = rememberClockTime(if (showSeconds) 1000L else 60_000L).value
) {
    val density = LocalDensity.current
    Canvas(
        modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val radius = min(cx, cy)
        val inset = radius * 0.06f
        val r = radius - inset

        // Face + rim
        drawCircle(color = colors.face, radius = r)
        drawCircle(color = colors.rim, radius = r, style = Stroke(width = r * 0.03f))

        // Ticks
        repeat(60) { i ->
            val isMajor = i % 5 == 0
            val tickLen = if (isMajor) r * 0.12f else r * 0.06f
            val tickWidth = if (isMajor) r * 0.014f else r * 0.006f
            val angleDeg = i * 6f - 90f
            val rad = Math.toRadians(angleDeg.toDouble())
            val sx = cx + cos(rad).toFloat() * (r - tickLen)
            val sy = cy + sin(rad).toFloat() * (r - tickLen)
            val ex = cx + cos(rad).toFloat() * r
            val ey = cy + sin(rad).toFloat() * r
            drawLine(
                color = if (isMajor) colors.tickMajor else colors.tickMinor,
                start = Offset(sx, sy),
                end = Offset(ex, ey),
                strokeWidth = tickWidth,
                cap = StrokeCap.Round
            )
        }

        // Angles
        val hour = time.hour % 12
        val minute = time.minute
        val second = time.second

        val hourAngle = ((hour) + minute / 60f + second / 3600f) * 30f - 90f
        val minuteAngle = (minute + second / 60f) * 6f - 90f
        val secondAngle = second * 6f - 90f

        fun hand(angleDeg: Float, length: Float, width: Float, color: Color, tail: Float = 0f) {
            val rad = Math.toRadians(angleDeg.toDouble())
            val ex = cx + cos(rad).toFloat() * (r * length)
            val ey = cy + sin(rad).toFloat() * (r * length)
            val tx = cx - cos(rad).toFloat() * (r * tail)
            val ty = cy - sin(rad).toFloat() * (r * tail)
            drawLine(color, Offset(tx, ty), Offset(ex, ey), width, cap = StrokeCap.Round)
        }

        // Hands
        hand(hourAngle, length = 0.55f, width = r * 0.04f, color = colors.hourHand, tail = 0.08f)
        hand(minuteAngle, length = 0.75f, width = r * 0.03f, color = colors.minuteHand, tail = 0.10f)
        if (showSeconds) {
            hand(secondAngle, length = 0.85f, width = r * 0.012f, color = colors.secondHand, tail = 0.15f)
        }

        // Center cap
        drawCircle(color = colors.center, radius = r * 0.03f)
    }
}

@Preview(showBackground = true)
@Composable
private fun AnalogClockPreview() {
    AnalogClock()
}