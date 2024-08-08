package com.nasenbaer.soundboard.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasenbaer.soundboard.ui.theme.SoundBoardTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ButtonCard(name: String, id: Int, onClick: () -> Unit, onLongPress: () -> Unit) {
    val selectedItems = remember { mutableListOf<Int>() }

    Row(
        Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = {}, onLongClick = { onLongPress() }),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(modifier = Modifier.padding(all = 24.dp), text = name)
        }
        ElevatedButton(
            onClick = { onClick() },
            elevation = ButtonDefaults.buttonElevation(8.dp),
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(text = "Play")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewButtonSection() {
    SoundBoardTheme {
        Column {
            ButtonCard(
                "Zonk!",
                5,
                onClick = { println("clicked") },
                onLongPress = { println("pressed") })
        }
    }
}