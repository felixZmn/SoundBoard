package com.nasenbaer.soundboard.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nasenbaer.soundboard.MainViewModel
import com.nasenbaer.soundboard.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSoundDialog(viewModel: MainViewModel, snackbarHostState: SnackbarHostState) {
    var pickedImageUri by remember { mutableStateOf<Uri?>(Uri.EMPTY) }
    var name by remember { mutableStateOf("") }
    var saveEnabled by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        pickedImageUri = if (it.resultCode == Activity.RESULT_CANCELED) {
            Uri.EMPTY
        } else {
            it.data?.data
        }
        saveEnabled = pickedImageUri != Uri.EMPTY && name != ""
    }

    val openSoundIntent = Intent(
        Intent.ACTION_OPEN_DOCUMENT, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    ).apply { addCategory(Intent.CATEGORY_OPENABLE) }

    Dialog(
        onDismissRequest = { viewModel.abort() }, properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold(topBar = {
                AddSoundTopBar({
                    if (viewModel.saveSound(pickedImageUri!!, name) < 0) {
                        CoroutineScope(Dispatchers.Main).launch {
                            snackbarHostState.showSnackbar(
                                context.getString(R.string.error_adding_sound)
                            )
                        }
                    }
                }, { viewModel.abort() }, saveEnabled
                )
            }) { innerPadding ->
                // padding to move below TopAppBar
                Column(modifier = Modifier.padding(innerPadding)) {
                    // padding to set layout paddings
                    Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)) {
                        TextField(value = name,
                            onValueChange = {
                                name = it
                                saveEnabled = pickedImageUri != Uri.EMPTY && name != ""
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(text = stringResource(id = R.string.add_sound_form_field_name)) })
                        TextField(
                            value = if (pickedImageUri.toString() == "null") "" else pickedImageUri.toString(),
                            enabled = false,
                            onValueChange = {/*won't do anything*/ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .clickable(onClick = {
                                    launcher.launch(openSoundIntent)
                                }),
                            label = {
                                Text(text = stringResource(id = R.string.add_sound_form_field_file))
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    launcher.launch(openSoundIntent)
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
private fun AddSoundTopBar(save: () -> Unit, abort: () -> Unit, saveEnabled: Boolean) {
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
        IconButton(onClick = { save() }, enabled = saveEnabled) {
            Icon(
                imageVector = Icons.Default.Save, contentDescription = "Save"
            )
        }
    })
}
