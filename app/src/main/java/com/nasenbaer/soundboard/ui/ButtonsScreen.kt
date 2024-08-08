package com.nasenbaer.soundboard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasenbaer.soundboard.BuildConfig
import com.nasenbaer.soundboard.data.Sound

@Composable
fun ButtonsScreen(contents: MutableList<Sound>) {
    Column(
        modifier = Modifier
            .padding(bottom = 20.dp) // order matters...
            .verticalScroll(rememberScrollState())
    ) {
        contents.forEach {
            ButtonCard(name = it.name.toString(), 5, onClick = it.play, onLongPress = it.delete) // ToDo: Fix
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(text = "Version: " + BuildConfig.VERSION_NAME, color = Color.Black.copy(alpha = 0.5f))
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewButtonScreen() {
    val data = mutableListOf(Sound(0, "Name", "path", {}, {}))
    ButtonsScreen(contents = data)
}