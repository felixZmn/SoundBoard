package com.nasenbaer.soundboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasenbaer.soundboard.ui.theme.SoundBoardTheme

@Composable
fun ButtonCard(name: String, modifier: Modifier = Modifier) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(modifier = Modifier.padding(all = 24.dp), text = name)
        }
        ElevatedButton(
            onClick = { /*ToDo*/ },
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
            ButtonCard("Zonk!")
            ButtonCard("Zonk!")
            ButtonCard("Zonk!")
            ButtonCard("Zonk!")
        }
    }
}