package com.nasenbaer.soundboard.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nasenbaer.soundboard.MainViewModel

@Composable
fun AddSoundDialog(save: () -> Unit, abort: () -> Unit) {
    Dialog(
        onDismissRequest = { save() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = "Hello Dialog")
            }
        }
    }
}

@Preview
@Composable
fun PreviewAddSoundDialog(){
    AddSoundDialog(save = {}, abort = {})
}
