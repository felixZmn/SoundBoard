package com.nasenbaer.soundboard.ui

import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nasenbaer.soundboard.BuildConfig

@Composable
fun ButtonsScreen(contents: Map<String, () -> Unit>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        contents.forEach {
            ButtonCard(name = it.key, onClick = it.value)
        }
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom) {
        Text(text = "Version: " + BuildConfig.VERSION_NAME, color = Color.Black.copy(alpha = 0.5f))
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewButtonScreen() {
    val data = mapOf("Button 1" to { print("Test 1") }, "Button 2" to { print("Test 2") })
    ButtonsScreen(contents = data)
}