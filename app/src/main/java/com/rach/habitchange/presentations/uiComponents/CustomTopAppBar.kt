package com.rach.habitchange.presentations.uiComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.rach.habitchange.R
import com.rach.habitchange.theme.HabitChangeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onNavigationIconClick: () -> Unit = {},
    titleStyle: TextStyle = MaterialTheme.typography.titleLarge,
    titleFontWeight: FontWeight = FontWeight.SemiBold
) {

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        title = {
            if (title != null) {
                Text(
                    text = title,
                    style = titleStyle,
                    fontWeight = titleFontWeight
                )
            } else {
                Text(text = stringResource(R.string.not_found))
            }
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = { onNavigationIconClick() }
            ) {
                when (title) {
                    "Home" -> Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = stringResource(R.string.menu_icon),
                        modifier = Modifier.size(dimensionResource(R.dimen.dimen_28dp))
                    )

                    else ->
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Arrow Back icon",
                            modifier = Modifier.size(dimensionResource(R.dimen.dimen_28dp))
                        )
                }
            }
        }
    )

}


@Preview
@Composable
private fun Preview() {
    HabitChangeTheme {
        CustomTopAppBar(
            modifier = Modifier.fillMaxWidth(),
            title = "Home",
            onNavigationIconClick = {}
        )
    }
}
