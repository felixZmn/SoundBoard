package com.nasenbaer.soundboard.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nasenbaer.soundboard.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSoundDialog(save: () -> Unit, abort: () -> Unit) {
    Dialog(
        onDismissRequest = { save() }, properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(topBar = {
                AddSoundTopBar(abort, save)
            }) { innerPadding ->
                // padding to move below TopAppBar
                Column(modifier = Modifier.padding(innerPadding)) {
                    // padding to set layout paddings
                    Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)) {
                        TextField(value = "",
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(text = stringResource(id = R.string.add_sound_form_field_name)) })
                        TextField(
                            value = "",
                            enabled = false,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .clickable(onClick = { println("clicked!") }),
                            label = {
                                Text(text = stringResource(id = R.string.add_sound_form_field_file))
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    println("Icon Button Clicked")
                                }) {
                                    Icon(
                                        Icons.Default.AudioFile,
                                        contentDescription = "Open audio File"
                                    )
                                }
                            },
                            // hack to create a clickable textfield
                            // see: https://issuetracker.google.com/issues/172154008
                            colors = TextFieldDefaults.textFieldColors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                //disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                        )

                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AddSoundTopBar(abort: () -> Unit, save: () -> Unit) {
    TopAppBar(colors = TopAppBarDefaults.largeTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    ), title = {
        Text(text = stringResource(id = R.string.add_sound))
    }, navigationIcon = {
        IconButton(onClick = { abort() }) {
            Icon(Icons.Default.Close, "Close Icon")
        }
    }, actions = {
        IconButton(onClick = { save() }) {
            Icon(
                imageVector = Icons.Default.Save, contentDescription = "Save"
            )
        }
    })
}


@Preview
@Composable
fun PreviewAddSoundDialog() {
    AddSoundDialog(save = {}, abort = {})
}
