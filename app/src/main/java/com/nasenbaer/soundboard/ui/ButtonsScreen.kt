package com.nasenbaer.soundboard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ButtonsScreen(contents: Map<String, () -> Unit>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        contents.forEach {
            ButtonCard(name = it.key, onClick = it.value)
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewButtonScreen() {
    val data = mapOf("Button 1" to { print("Test 1") }, "Button 2" to { print("Test 2") })
    ButtonsScreen(contents = data)
}