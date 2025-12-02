package com.rach.habitchange.db.apps

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

    // yeah tumara live result dega
    @Query("SELECT * FROM AppEntity")
    fun getAll(): Flow<List<AppEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(apps: List<AppEntity>)

    @Query("DELETE FROM AppEntity WHERE id = :id")
    suspend fun delete(id: Int)


}