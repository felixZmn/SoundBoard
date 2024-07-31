package com.nasenbaer.soundboard

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

    fun saveSound(path: Uri, name: String): Int {
        val context = getApplication<Application>().applicationContext
        val contentResolver = context.contentResolver

        showDialog.value = false

        var fileName = ""
        contentResolver.query(path, null, null, null, null, null)?.use {
            if (!it.moveToFirst()) {
                return -1
            }
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index < 0) {
                return -1
            }
            fileName = it.getString(index)
        }

        if (name == "" || path.toString() == "") return -1

        val inputStream = contentResolver.openInputStream(path)
        val bytes = inputStream?.readBytes() ?: return -1

        val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fos.write(bytes)
        fos.close()

        viewModelScope.launch {
            val newSound = Sound(name, fileName)
            val id = AppDatabase.getInstance(context).soundDao().insertAll(newSound)
            newSound.id = id[0].toInt()
            soundsList.add(newSound)
        }

        return 0
    }

    fun abort() {
        showDialog.value = false
    }

    fun play(id: Int) {
        // stop player
        if (this.player.isPlaying) {
            this.player.stop()
            return
        }

        val context = getApplication<Application>().applicationContext

        // load file name to play
        val fileName = runBlocking {
            coroutineScope {
                AppDatabase.getInstance(context).soundDao().getByIds(arrayOf(id))[0].path
            }
        } ?: return

        this.currentId = id
        // prepare & launch player

        val uri = Uri.parse(context.filesDir.toString() + File.separatorChar + fileName)
        this.player.setMediaItem(MediaItem.fromUri(uri))
        this.player.prepare()
        this.player.play()
    }

    suspend fun loadSounds() {
        val deferred: Deferred<List<Sound>> = viewModelScope.async {
            AppDatabase.getInstance(getApplication<Application>().applicationContext).soundDao()
                .getAll()
        }
        val data = deferred.await()

        data.forEach { sound -> soundsList.add(sound) }
    }
}