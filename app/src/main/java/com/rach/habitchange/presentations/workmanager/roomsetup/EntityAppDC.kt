package com.rach.habitchange.presentations.workmanager.roomsetup

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "selected_app")
data class EntityAppDC(
    @PrimaryKey val packageName: String,
    val limitInMin : Long
)
