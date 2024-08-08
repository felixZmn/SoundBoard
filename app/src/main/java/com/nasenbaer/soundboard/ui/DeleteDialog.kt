package com.nasenbaer.soundboard.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DeleteDialog(onConfirmation: () -> Unit, onDismiss: () -> Unit, soundName: String) {
    AlertDialog(
        icon = {
            Icon(Icons.Default.DeleteForever, contentDescription = "Delete Icon")
        },
        title = {
            Text(text = "Löschen")
        },
        text = {
            Text(text = "Sound $soundName endgültig löschen?")
        },
        onDismissRequest = {
            onDismiss()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Bestätigen")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Abbrechen")
            }
        }
    )

}