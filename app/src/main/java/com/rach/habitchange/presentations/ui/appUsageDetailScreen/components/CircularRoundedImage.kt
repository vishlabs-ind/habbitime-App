package com.rach.habitchange.presentations.ui.appUsageDetailScreen.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun CircularRoundedImage(
    modifier: Modifier = Modifier,
    brushWidth: Float = 12f,
    radius: Dp,
    appIcon: Any
) {

    val infiniteTransition = rememberInfiniteTransition()
    val rotateAnimation = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(1000, easing = LinearEasing))
    )

    val brush = Brush.horizontalGradient(
        colors = listOf(
            Color.Yellow, //Yellow
            Color(0xFFFF6F00), // Orange
            Color(0xFFFF8C00), // Darker Orange
            Color(0xFFFF2D55), // Pinkish Red
            Color(0xFF8E2DE2), // Purple
            Color(0xFF4A00E0).copy(alpha = 0.5f), // Indigo Purple
            Color(0xFF00C9FF)  // Aqua Blue)
        )
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            rotate(rotateAnimation.value) {
                drawCircle(
                    brush = brush,
                    style = Stroke(brushWidth),
                    radius = (radius / 2).toPx() + 5.dp.toPx()
                )
            }
        }

        AsyncImage(
            model = appIcon,
            contentDescription = null,
            modifier = Modifier
                .size(radius)
                .clip(CircleShape)
        )
    }
}
