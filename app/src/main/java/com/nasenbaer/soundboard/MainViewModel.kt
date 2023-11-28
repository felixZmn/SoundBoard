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
    private val player = ExoPlayer.Builder(application.applicationContext).build()

    fun play(id: Int) {
        if (this.player.isPlaying){
            this.player.stop()
        }
        this.player.setMediaItem(MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(id)))
        this.player.prepare()
        this.player.play()
    }

    fun getSounds(): Map<Int, String> {
        return mapOf(R.raw.zonk to "Zonk!", R.raw.badumtss to "Badum")
    }
}