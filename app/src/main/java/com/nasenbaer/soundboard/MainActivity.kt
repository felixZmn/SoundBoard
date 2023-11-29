package com.nasenbaer.soundboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.nasenbaer.soundboard.ui.theme.SoundBoardTheme

/*
 * Note:
 * Needed permission to import sounds: ACTION_GET_CONTENT
 * further reading: https://developer.android.com/topic/libraries/architecture/datastore
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            SoundBoardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    SoundBoardApp(viewModel = viewModel)
                }
            }
        }
    }
}

