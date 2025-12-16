package com.rach.habitchange.presentations.workmanager.roomsetup

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface Idao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLimit(limit: EntityAppDC)

    @Query("SELECT * FROM selected_app WHERE packageName = :packageName")
    suspend fun getLimit(packageName: String): EntityAppDC?

}