package com.nasenbaer.soundboard

import android.app.Application
import androidx.annotation.OptIn
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer

@OptIn(UnstableApi::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val application : Application
    val player = ExoPlayer.Builder(application.applicationContext).build()

    init {
        this.application = application
    }

    fun getSounds(): Map<Int, String> {
        return mapOf(R.raw.zonk to "Zonk!", R.raw.badumtss to "Badum", R.raw.jeopardy to "Jeopardy", R.raw.shutdown to "Shutdown")
    }
}