package com.rach.habitchange.presentations.uiComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.rach.habitchange.AppPreview
import com.rach.habitchange.R
import com.rach.habitchange.theme.HabitChangeTheme

@Composable
fun CircularConfirmButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageContent: @Composable () -> Unit,
    buttonColor: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        colors = buttonColor,
        contentPadding = PaddingValues(15.dp)
    ) {
        imageContent()
    }
}

@AppPreview
@Composable
private fun Preview() {
    HabitChangeTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CircularConfirmButton(
                onClick = {},
                imageContent = {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(
                            dimensionResource(R.dimen.dimen_28dp)
                        )
                    )
                }
            )
        }
    }
}
