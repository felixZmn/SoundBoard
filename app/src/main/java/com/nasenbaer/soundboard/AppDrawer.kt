package com.nasenbaer.soundboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlin.math.roundToInt

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppDrawer(
    drawerState: DrawerState,
    currentScreen: MainScreen,
    coroutineScope: CoroutineScope,
    viewModel: MainViewModel,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        val volume = remember { mutableStateOf(0f) }
        val checked = remember { mutableStateOf(true) }
        val sliderEnabled = remember { mutableStateOf(true) }

        DrawerSheet(checked, sliderEnabled, volume)
    }) {
        content()
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DrawerSheet(
    checked: MutableState<Boolean>,
    sliderEnabled: MutableState<Boolean>,
    volume: MutableState<Float>
) {
    ModalDrawerSheet {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Default icon",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "Lautstärke",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            Checkbox(checked = checked.value, onCheckedChange = {
                checked.value = it
                sliderEnabled.value = it
            })
            Text(
                text = stringResource(id = R.string.use_system_volume), textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            Icon(
                Icons.Default.VolumeUp, "Volume Icon",
                Modifier
                    .size(48.dp)
                    .padding(all = 12.dp)
            )
            Slider(
                value = volume.value,
                onValueChange = { volume.value = it.roundToInt().toFloat() },
                valueRange = 0f..100f,
                enabled = sliderEnabled.value
            )
        }

    }
}
