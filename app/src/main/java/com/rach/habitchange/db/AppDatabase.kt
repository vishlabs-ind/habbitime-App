package com.rach.habitchange.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rach.habitchange.db.apps.AppDao
import com.rach.habitchange.db.apps.AppEntity

@Database(
    entities = [AppEntity::class],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habit_change_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}