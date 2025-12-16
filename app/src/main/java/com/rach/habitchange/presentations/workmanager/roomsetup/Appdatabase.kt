package com.rach.habitchange.presentations.workmanager.roomsetup

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [EntityAppDC::class], version = 1, exportSchema = false)
abstract class Appdatabase : RoomDatabase() {

    abstract fun daolimit(): Idao
}