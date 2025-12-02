package com.rach.habitchange.presentations.uiComponents.timeSelect


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rach.habitchange.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun BaseDurationPicker(
    modifier: Modifier = Modifier,
    current: Int,
    minimumSeconds: Int,
    maximumSeconds: Int,
    onConfirmClick: (String) -> Unit,
    confirmButtonTitle: String? = null,
    confirmButtonTopPadding: Dp = 36.dp
) {

    val currentValue =
        remember(current) { TimeUtil.convertSecondToDuration(current) }

    BasePickerImpl(
        modifier = modifier,
        current = currentValue,
        minimumSeconds = minimumSeconds,
        maximumSeconds = maximumSeconds,
        onConfirmClick = onConfirmClick,
        confirmButtonTitle = confirmButtonTitle ?: stringResource(
            id = R.string.duration_picker_confirm_button
        ),
        confirmButtonTopPadding = confirmButtonTopPadding
    )
}

@OptIn(FlowPreview::class)
@Composable
private fun BasePickerImpl(
    modifier: Modifier,
    current: Pair<Int, Int>,
    minimumSeconds: Int,
    maximumSeconds: Int,
    confirmButtonTitle: String,
    onConfirmClick: (String) -> Unit,
    confirmButtonTopPadding: Dp = 36.dp
) {
    val minMinute =
        remember(minimumSeconds) { TimeUtil.convertSecondToDuration(minimumSeconds).second }
    val maxMinute =
        remember(maximumSeconds) { TimeUtil.convertSecondToDuration(maximumSeconds).second }
    val minHour =
        remember(minimumSeconds) { TimeUtil.convertSecondToDuration(minimumSeconds).first }
    val maxHour =
        remember(maximumSeconds) { TimeUtil.convertSecondToDuration(maximumSeconds).first }
    var selectedHour by remember(current) { mutableIntStateOf(current.first) }
    var selectedMinute by remember(current) { mutableIntStateOf(current.second) }
    val hours = remember { convertToMinMaxHour(minHour, maxHour) }
    var minutes by remember(current) {
        mutableStateOf<List<Int>>(mutableListOf<Int>().apply {
            when (selectedHour) {
                TimeUtil.convertSecondToDuration(minimumSeconds).first -> {
                    addAll((minMinute..59))
                }

                TimeUtil.convertSecondToDuration(maximumSeconds).first -> {
                    addAll((0..maxMinute))
                }

                else -> {
                    addAll((0..59))
                }
            }
        })
    }
    var scope = rememberCoroutineScope()
    val hourListState = rememberWheelPickerState(hours.indexOf(selectedHour).coerceAtLeast(0))
    val minuteListState =
        rememberWheelPickerState(minutes.indexOf(selectedMinute).coerceAtLeast(0))



    LaunchedEffect(selectedHour) {
        minutes = convertToMinMaxMinute(
            minimumSeconds,
            maximumSeconds,
            minMinute,
            maxMinute,
            selectedHour,
            selectedMinute,
            minuteListState
        )
    }
    LaunchedEffect(hourListState) {
        scope.launch {
            snapshotFlow { hourListState.currentIndexSnapshot }
                .debounce(100.milliseconds)
                .collect {
                    selectedHour = hours[it]
                }
        }
    }
    LaunchedEffect(minuteListState) {
        scope.launch {
            snapshotFlow { minuteListState.currentIndexSnapshot }
                .debounce(100.milliseconds)
                .collect {
                    selectedMinute = minutes[it]
                }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = 4.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            BaseVerticalWheelPicker(
                modifier = Modifier
                    .width(40.dp)
                    .wrapContentHeight(),
                unfocusedCount = 1,
                items = hours,
                state = hourListState,
                focus = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()

                    )
                },
                content = { index ->
                    Text(
                        textAlign = TextAlign.Center,
                        text = hours[index].padWithZero(),
                        style = TextStyle.Default,
                        fontFamily = FontFamily(
                            Font(R.font.f_pro_display_bold),
                            Font(R.font.f_pro_display_bold, FontWeight.Bold)
                        ),
                        color = Color(0xFF222121)
                    )
                })

            Text(
                text = stringResource(id = R.string.hour_indicator),
                color = Color(0xFF222121),
                style = TextStyle.Default,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(
                    Font(R.font.f_pro_display_bold),
                    Font(R.font.f_pro_display_bold, FontWeight.Bold)
                )
            )
            VerticalDivider(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 6.dp)
                    .width(1.dp)
                    .fillMaxHeight(),
                color = Color(0xFFCCCCCC)
            )
            BaseVerticalWheelPicker(
                modifier = Modifier
                    .width(40.dp)
                    .wrapContentHeight(),
                unfocusedCount = 1,
                items = minutes,
                state = minuteListState,
                focus = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()

                    )
                },
                content = { index ->
                    Text(
                        textAlign = TextAlign.Center,
                        text = minutes[index].padWithZero(),
                        style = TextStyle.Default,
                        fontFamily = FontFamily(
                            Font(R.font.f_pro_display_bold),
                            Font(R.font.f_pro_display_bold, FontWeight.Bold)
                        ),
                        color = Color(0xFF222121)
                    )
                })
            Text(
                text = stringResource(id = R.string.minute_indicator),
                color = Color(0xFF222121),
                style = TextStyle.Default,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(
                    Font(R.font.f_pro_display_bold),
                    Font(R.font.f_pro_display_bold, FontWeight.Bold)
                )
            )

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = confirmButtonTopPadding, bottom = 20.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .clickable { onConfirmClick(formatTime(selectedHour, selectedMinute)) }
                .background(color = Color(0xFF181717)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = confirmButtonTitle,
                style = TextStyle.Default,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(
                    Font(R.font.f_pro_display_bold),
                    Font(R.font.f_pro_display_bold, FontWeight.Bold)
                ),
                color = Color.White
            )
        }
    }


}

private fun convertToMinMaxHour(minHour: Int, maxHour: Int): List<Int> {


    return mutableListOf<Int>().apply {
        addAll((minHour..maxHour))
    }
}

private suspend fun convertToMinMaxMinute(
    minimumSeconds: Int,
    maximumSeconds: Int,
    minMinute: Int,
    maxMinute: Int,
    selectedHour: Int,
    selectMinute: Int,
    minuteState: WheelPickerState
): List<Int> {
    return mutableListOf<Int>().apply {
        when (selectedHour) {
            TimeUtil.convertSecondToDuration(minimumSeconds).first -> {
                addAll((minMinute..59))
            }

            TimeUtil.convertSecondToDuration(maximumSeconds).first -> {
                addAll((0..maxMinute))
            }

            else -> {
                addAll((0..59))
            }
        }
    }.also {
        val index = it.indexOf(selectMinute)
        if (index == -1) {
            minuteState.scrollToIndex(0)
        } else {
            minuteState.scrollToIndex(index)
        }
    }
}

private fun Int.padWithZero(): String = if (this < 10) "0$this" else toString()
private fun formatTime(hour: Int, minute: Int): String {
    return "${hour.padWithZero()}:${minute.padWithZero()}:00"
}

@Preview(showBackground = true)
@Composable
private fun PreviewHourNumberPicker(
) {
    var time by remember { mutableStateOf("00:00:00") }
    BaseDurationPicker(
        current = (15 * 60) + (60 * 60),
        minimumSeconds = (15 * 60) + (60 * 60),
        maximumSeconds = (3 * 60 * 60) + (45 * 60),
        onConfirmClick = { time = it },
        confirmButtonTitle = null
    )
}