package com.nasenbaer.soundboard.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase


@Database(entities = [Sound::class], version=1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun soundDao(): SoundDAO

    companion object {
        private lateinit var db : AppDatabase
        fun getInstance(context: Context): AppDatabase {
            if (!this::db.isInitialized){
                db = databaseBuilder(context, AppDatabase::class.java, "sound").build()
            }
            return db
        }
    }
}
