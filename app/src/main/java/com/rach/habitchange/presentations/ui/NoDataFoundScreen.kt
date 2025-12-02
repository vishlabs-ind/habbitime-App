package com.rach.habitchange.presentations.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.rach.habitchange.R
import com.rach.habitchange.theme.HabitChangeTheme
import com.rach.habitchange.utils.Secrets

@Composable
fun NoDataFound(
    modifier: Modifier = Modifier,
    text: String,
    text2: String? = null
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.undraw_taken_mshk),
            contentDescription = null,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.dimen_16dp))
        )

        Text(text,
            modifier = Modifier.padding(bottom = dimensionResource(R.dimen.dimen_50dp)))
        if (text2 != null) {
            Text(
                text2
            )
        }
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    setAdSize(AdSize.BANNER)
                    adUnitId = Secrets.ADS_UNIT_ID
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    HabitChangeTheme {
        NoDataFound(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp),
            text = "No App Found"
        )
    }
}