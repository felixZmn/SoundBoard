package com.nasenbaer.soundboard.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sound (
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "name") var name:String?,
    @ColumnInfo(name="path") var path:String?
){
    constructor(name: String?, path: String?) : this(0, name, path)
}
