package com.rach.habitchange.db.apps

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("AppEntity")
data class AppEntity(
    @PrimaryKey
    val id:Int,
    val name: String,
    val packageName :String
)

