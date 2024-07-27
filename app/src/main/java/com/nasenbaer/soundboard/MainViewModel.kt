package com.nasenbaer.soundboard

import android.app.Application
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.MutableState
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer

@OptIn(UnstableApi::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var showDialog: MutableState<Boolean>
    private val player = ExoPlayer.Builder(application.applicationContext).build()
    private var currentId = 0


    fun save(path: Uri, name: String){
        showDialog.value = false
        println("save")
        println("Path: $path")
        println("Name: $name")
    }

    fun abort(){
        showDialog.value = false
        println("abort")
    }

    fun play(id: Int) {
        if (this.player.isPlaying) {
            this.player.stop()
        }
        if (id != currentId) {
            this.currentId = id
            this.player.setMediaItem(MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(id)))
            this.player.prepare()
            this.player.play()
        }
    }

    fun getSounds(): Map<Int, String> {
        return mapOf(
            R.raw.zonk to "Zonk!",
            R.raw.badumtss to "Badum",
            R.raw.jeopardy to "Jeopardy!",
            R.raw.shutdown to "Shutdown"
        )
    }
}