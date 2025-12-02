package com.rach.habitchange.presentations.uiComponents

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PermissionDialog(
    onDismissRequest: () -> Unit,
    onConfirmButton: () -> Unit,
    title: String,
    text: String
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmButton()
                }
            ) {
                Text("Go To Settings")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        },
        title = {
            Text(title)
        },
        text = {
            Text(text)
        }
    )

}