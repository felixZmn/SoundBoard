package com.nasenbaer.soundboard

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.annotation.OptIn
import androidx.compose.runtime.MutableState
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.nasenbaer.soundboard.data.AppDatabase
import com.nasenbaer.soundboard.data.Sound
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File


@OptIn(UnstableApi::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var showDialog: MutableState<Boolean>
    lateinit var soundsList: MutableList<Sound>
    private val player = ExoPlayer.Builder(application.applicationContext).build()
    private var currentId = 0


    @SuppressLint("Range")
    fun save(path: Uri, name: String){
        val context = getApplication<Application>().applicationContext
        val contentResolver = context.contentResolver

        showDialog.value = false

        var displayName = ""

        contentResolver.query(path, null, null, null, null, null)?.use {
            if (it.moveToFirst()){
                displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }

        val bytes = contentResolver.openInputStream(path)?.readBytes()
        val fos = context.openFileOutput(displayName, Context.MODE_PRIVATE)
        fos.write(bytes)
        fos.close()

        viewModelScope.launch {
            var newSound = Sound(name, displayName)
            var id = AppDatabase
                .getInstance(context)
                .soundDao()
                .insertAll(newSound)
            newSound.id = id[0].toInt()
            soundsList.add(newSound)
        }
    }

    fun abort(){
        showDialog.value = false
        println("abort")
    }

    fun play(id: Int) {
        // stop player
        if (this.player.isPlaying) {
            this.player.stop()
            return
        }

        val context = getApplication<Application>().applicationContext

        // load file name to play
        val fileName = runBlocking { coroutineScope {
            AppDatabase.getInstance(context).soundDao().getByIds(arrayOf(id))[0].path
        } } ?: return

        this.currentId = id
        // prepare & launch player

        val uri = Uri.parse(context.filesDir.toString() + File.separatorChar + fileName)
        this.player.setMediaItem(MediaItem.fromUri(uri))
        this.player.prepare()
        this.player.play()
    }

    suspend fun getSounds(): List<Sound> {
        val deferred: Deferred<List<Sound>> = viewModelScope.async { AppDatabase.getInstance(getApplication<Application>().applicationContext).soundDao().getAll() }
        return deferred.await()
    }
}