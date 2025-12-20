package com.rach.habitchange.presentations.ui.appUsageDetailScreen.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rach.habitchange.R
import com.rach.habitchange.presentations.ui.appUsageDetailScreen.isNotificationEnabled
import com.rach.habitchange.presentations.ui.homescreen.minToHourMinute
import com.rach.habitchange.theme.HabitChangeTheme
import com.rach.habitchange.theme.poppinsBoldFont
import com.rach.habitchange.theme.poppinsMediumFont

@Composable
fun TodayUsageTextAppDetailsScreen(
    modifier: Modifier = Modifier,
    todayUsage: Long,
    onSetLimitClick: () -> Unit = {}
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.outline_chart_data_24),
                contentDescription = stringResource(R.string.today_usage_icon),
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.dimen_8dp)))
            Text(
                text = stringResource(R.string.today_usage),
                style = poppinsMediumFont.copy(
                    fontSize = 20.sp
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = minToHourMinute(todayUsage),
                style = poppinsBoldFont.copy(
                    fontSize = 35.sp
                ),
                modifier = Modifier.padding(start = 5.dp)
            )

            val context = LocalContext.current

            Button(
                onClick = {
                    if (isNotificationEnabled(context)) {
                        onSetLimitClick()
                    } else {
                        Toast.makeText(
                            context,
                            "Please enable notifications to set limit",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            ) {
                Text(
                    text = "Set Limit",
                    style = poppinsBoldFont.copy(
                        fontSize = 15.sp
                    )
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    HabitChangeTheme {
        TodayUsageTextAppDetailsScreen(
            modifier = Modifier.fillMaxWidth(),
            todayUsage = 4000
        )
    }
}