package com.rach.habitchange.presentations.ui.selectApp.components

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.rach.habitchange.R
import com.rach.habitchange.theme.HabitChangeTheme

@Composable
fun SelectAppSingleDesign(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    packageName: String,
    appName: String
) {

    val context = LocalContext.current
    val appIcon = remember(packageName) {
        try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: Exception) {
            null
        }
    }

    Row(
        modifier = modifier
            .then(
                if (selected) {
                    Modifier
                        .background(
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                } else {
                    Modifier
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        appIcon?.let {
            ImageDesign(
                appIcon = it,
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
            )
        }

        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.dimen_20dp)))
        Text(
            text = appName,
            style = MaterialTheme.typography.titleMedium
        )

    }

}

@Composable
private fun ImageDesign(
    modifier: Modifier = Modifier,
    appIcon: Drawable?
) {

    if (appIcon != null) {
        SubcomposeAsyncImage(
            model = appIcon,
            contentDescription = null,
            error = {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null
                )
            },
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = modifier.size(48.dp),
            contentScale = ContentScale.Crop
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    HabitChangeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SelectAppSingleDesign(
                modifier = Modifier.fillMaxWidth(),
                packageName = "com.google.android.youtube",
                appName = "Telegram"
            )
        }
    }
}