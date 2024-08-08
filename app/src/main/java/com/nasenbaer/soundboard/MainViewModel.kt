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
import kotlinx.coroutines.launch
import java.io.File


@OptIn(UnstableApi::class)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var showAddSoundDialog: MutableState<Boolean>
    lateinit var soundsList: MutableList<Sound>
    private val player = ExoPlayer.Builder(application.applicationContext).build()
    lateinit var selectedSound: Sound

    fun saveSound(path: Uri, name: String): Int {
        val context = getApplication<Application>().applicationContext
        val contentResolver = context.contentResolver

        showAddSoundDialog.value = false

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

        // add timestamp for unique file names
        fileName = System.currentTimeMillis().toString() + fileName

        if (name == "" || path.toString() == "") return -1

        val inputStream = contentResolver.openInputStream(path)
        val bytes = inputStream?.readBytes() ?: return -1

        val fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        fos.write(bytes)
        fos.close()

        viewModelScope.launch {
            val newSound = Sound(name, context.filesDir.toString() + File.separatorChar + fileName)
            val id = AppDatabase.getInstance(context).soundDao().insertAll(newSound)
            newSound.id = id[0].toInt()
            soundsList.add(newSound)
        }

        return 0
    }

    fun abort() {
        showAddSoundDialog.value = false
    }

    fun playSelected() {
        // stop player
        if (this.player.isPlaying) {
            this.player.stop()
            return
        }

        // prepare & launch player
        this.player.setMediaItem(MediaItem.fromUri(Uri.parse(selectedSound.path)))
        this.player.prepare()
        this.player.play()
    }

    fun deleteSelected() {
        val file = File(selectedSound.path!!)
        if (file.exists()){
            file.delete()
        }

        val context = getApplication<Application>().applicationContext
        viewModelScope.launch {
            AppDatabase.getInstance(context).soundDao().delete(selectedSound)
            soundsList.removeAt(soundsList.indexOf(selectedSound))
        }


        println("Deleted sound ${selectedSound.name} with id ${selectedSound.id}")
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