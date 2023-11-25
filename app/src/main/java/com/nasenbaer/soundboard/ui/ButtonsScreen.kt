package com.nasenbaer.soundboard.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.nasenbaer.soundboard.MainViewModel

@Composable
fun ButtonsScreen(viewModel: MainViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        viewModel.getSounds().forEach {
            ButtonCard(name = it.value) { viewModel.play() }
        }
    }
}