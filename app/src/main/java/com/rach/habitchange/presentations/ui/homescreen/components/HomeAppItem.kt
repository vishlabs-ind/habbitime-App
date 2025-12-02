package com.rach.habitchange.presentations.ui.homescreen.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.rach.habitchange.AppPreview
import com.rach.habitchange.R
import com.rach.habitchange.theme.HabitChangeTheme

@Composable
fun HomeAppItem(
    modifier: Modifier = Modifier,
    appName: String = "Telegram",
    packageName: String,
    usageTime: String = "7 hrs",
    rank: Int = 1,
    onClick: () -> Unit = {},
    darkTheme: Boolean = isSystemInDarkTheme()
) {
    val context = LocalContext.current
    val appIcon = remember(packageName) {
        try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: Exception) {
            null
        }
    }
    val scale = remember { Animatable(1f) }

    val progressIndicatorColor = if (darkTheme) {
        Color.White.copy(alpha = 0.3f)
    } else {
        Color.Blue.copy(alpha = 0.2f)
    }

    val usageHours: Float = remember(usageTime) {
        val hrRegex = Regex("(\\d+)\\s*hr")
        val minRegex = Regex("(\\d+)\\s*min")

        val hrMatch = hrRegex.find(usageTime)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f
        val minMatch = minRegex.find(usageTime)?.groupValues?.get(1)?.toFloatOrNull() ?: 0f

        val totalHours = hrMatch + (minMatch / 60f)
        totalHours
    }

    val progress = (usageHours / 12f).coerceIn(0f, 1f)


    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1.02f,
            animationSpec = tween(durationMillis = 300)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300)
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale.value)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.normal_padding)),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Text(
//                text = "$rank ",
//                fontSize = 20.sp,
//                modifier = Modifier
//                    .padding(end = dimensionResource(R.dimen.dimen_10dp)),
//                color = MaterialTheme.colorScheme.primary,
//                fontWeight = FontWeight.Bold
//            )

            SubcomposeAsyncImage(
                model = appIcon,
                contentDescription = "$appName icon",
                modifier = Modifier
                    .size(dimensionResource(R.dimen.dimen_30dp))
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .shadow(2.dp, CircleShape)
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.normal_padding)))

            NameAndProgress(
                appName = appName,
                progress = progress,
                modifier = Modifier.weight(1f),
                progressIndicatorColor = progressIndicatorColor
            )

            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.dimen_8dp)))

            UsageText(
                usageTime = usageTime,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(top = dimensionResource(R.dimen.dimen_3dp))
            )
        }
    }
}

@Composable
private fun NameAndProgress(
    appName: String,
    progress: Float,
    progressIndicatorColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = appName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_8dp)))
        LinearProgressIndicator(
            progress = {
                progress
            },
            modifier = Modifier.fillMaxWidth(),
            strokeCap = StrokeCap.Round,
            color = Color.Blue,
            trackColor = progressIndicatorColor
        )


    }
}

@Composable
private fun UsageText(
    usageTime: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = usageTime,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Today",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@AppPreview
@Composable
private fun Preview() {
    HabitChangeTheme {
        HomeAppItem(
            appName = "Telegram",
            packageName = "com.google.com",
            usageTime = "7 hrs",
            rank = 1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.normal_padding))
        )
    }
}