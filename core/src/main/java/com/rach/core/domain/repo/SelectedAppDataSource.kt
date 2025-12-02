package com.rach.core.domain.repo

import com.rach.core.data.model.SelectedAppInfoCore
import kotlinx.coroutines.flow.Flow

/**
 * RepoImp of This Interface is in ( app -- db --- apps -- AppDataSource that is SelectAppDataSource Imp )
 */
interface SelectedAppDataSource {

    fun load(): Flow<List<SelectedAppInfoCore>>

    suspend fun save(apps: List<SelectedAppInfoCore>)

    suspend fun delete(id:Int)

}