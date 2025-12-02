package com.rach.habitchange.presentations.ui.appUsageDetailScreen.components

import android.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.rach.habitchange.R
import com.rach.habitchange.presentations.model.LoadAppDataWithUsage
import com.rach.habitchange.presentations.ui.homescreen.minToHourMinute
import com.rach.habitchange.theme.poppinsSemiBoldFont
import kotlin.math.ceil

@Composable
fun FiveDaysDataUiSection(
    modifier: Modifier = Modifier,
    fiveDaysAppUsageData: List<LoadAppDataWithUsage>
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Canvas(
                modifier = Modifier
                    .graphicsLayer { clip = false }
                    .drawBehind {
                        drawIntoCanvas { canvas ->
                            val paint = Paint().asFrameworkPaint().apply {
                                isAntiAlias = true
                                color = Color.RED
                                setShadowLayer(50f, 0f, 0f, "#FF6F00".toColorInt())
                            }

                            canvas.nativeCanvas.drawCircle(
                                size.width / 2,
                                size.height / 2,
                                12f,
                                paint
                            )
                        }
                    }) {

            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Previous Days Usages")
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_15dp)))
        if (fiveDaysAppUsageData.isEmpty()) {
            Text("No Data Found", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            UsageBarGraph(data = fiveDaysAppUsageData)
            Spacer(Modifier.height(10.dp))
            fiveDaysAppUsageData.forEach { data ->
                data.date?.let {
                    Text(
                        text = it,
                        style = poppinsSemiBoldFont
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "👉🏻 ${minToHourMinute(data.todayUsageInMinutes)}",
                    modifier.padding(start = 5.dp, bottom = 10.dp)
                )
            }
        }
    }
}

@Composable
fun UsageBarGraph(
    data: List<LoadAppDataWithUsage>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    val maxMinutes = data.maxOf { it.todayUsageInMinutes }
    val maxHours = ceil(maxMinutes / 60f).toInt().coerceAtLeast(1)

    val barWidth = 30.dp
    val barColor = androidx.compose.ui.graphics.Color(0xFF2196F3)
    val relativeLabels = data
//        .asReversed()
        .map { it.date ?: "" }

    val totalHeight = 200.dp
    val labelAreaHeight = 24.dp

    val graphAreaHeight = totalHeight - labelAreaHeight

    val density = LocalDensity.current
    val graphHeightPx = with(density) { graphAreaHeight.toPx() }
    val step = graphHeightPx / maxHours

    Box(
        modifier = modifier
            .padding(10.dp)
            .height(totalHeight)
    ) {

        Canvas(
            modifier = Modifier
                .matchParentSize()
                .padding(bottom = labelAreaHeight)
        ) {
            val textPaint = android.graphics.Paint().apply {
                textSize = with(density) { 10.sp.toPx() }
                color = android.graphics.Color.BLACK
                isAntiAlias = true
            }

            val textPadding = 20.dp.toPx()
            val textGap = 10.dp.toPx()
//            val rightPadding = 30.dp.toPx() // space so bars won't touch labels


            for (h in 0..maxHours) {
                val y = size.height - (h * step)


                drawLine(
                    color = androidx.compose.ui.graphics.Color.LightGray.copy(alpha = 0.4f),
                    start = Offset(0f, y),
                    end = Offset(size.width - textPadding, y),
                    strokeWidth = 2f
                )

                drawContext.canvas.nativeCanvas.drawText(
                    "${h}h",
                    size.width - textPaint.measureText("${h}h"), // Aligned to far right edge
                    y - 5.dp.toPx(),
                    textPaint
                )
            }
        }

        // Bars & Labels
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom
        ) {
            // Using a spacer to align with the Y-Axis labels on the right if needed,
            // otherwise just distribute weights.
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Bottom
            ) {
                data.forEachIndexed { index, item ->
                    val usage = item.todayUsageInMinutes
                    // Calculate ratio based on maxHours
                    val heightRatio = (usage / (maxHours * 60f)).coerceIn(0f, 1f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f) // Distribute evenly
                            .fillMaxHeight()
                            .padding(end = 15.dp)// Fill the Box height
                    ) {
                        // 2. The Graph Portion (Bars)
                        // This Box takes all available space above the text
                        Box(
                            modifier = Modifier
                                .weight(1f) // Fill remaining space
                                .fillMaxWidth(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            val barHeight = graphAreaHeight * heightRatio

                            Canvas(
                                modifier = Modifier
                                    .height(barHeight)
                                    .width(barWidth)
                            ) {
                                drawRect(color = barColor)
                            }
                        }

                        // 3. The Label Portion
                        // Placed outside the weight(1f) box, with fixed height
                        Box(
                            modifier = Modifier
                                .height(labelAreaHeight)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = relativeLabels.getOrElse(index) { "" },
                                fontSize = 10.sp,
                                color = androidx.compose.ui.graphics.Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}