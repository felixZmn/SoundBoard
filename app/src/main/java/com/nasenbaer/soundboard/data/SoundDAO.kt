package com.nasenbaer.soundboard.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SoundDAO {
    @Query("SELECT * FROM sound")
    suspend fun getAll(): List<Sound>

    @Query("SELECT * FROM sound WHERE id IN (:soundIds)")
    suspend fun getByIds(soundIds: Array<Int>): List<Sound>

    @Insert
    suspend fun insertAll(vararg users: Sound) : Array<Long>

    @Delete
    suspend fun delete(sound: Sound)
}
