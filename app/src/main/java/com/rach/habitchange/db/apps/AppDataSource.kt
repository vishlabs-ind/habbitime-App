package com.rach.habitchange.db.apps

import com.rach.core.data.model.SelectedAppInfoCore
import com.rach.core.domain.repo.SelectedAppDataSource
import com.rach.habitchange.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AppDataSource @Inject constructor(
    private val appDatabase: AppDatabase
) : SelectedAppDataSource {
    override fun load(): Flow<List<SelectedAppInfoCore>> {
        return read().map {list ->
            list.sortedBy {
                it.id
            }
        }

    }

    override suspend fun save(apps: List<SelectedAppInfoCore>) {
        try {
            val entity = apps.map {
                AppEntity(
                    id = it.id,
                    name = it.name,
                    packageName = it.packageName
                )
            }
            appDatabase.appDao().insert(entity)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override suspend fun delete(id: Int) {
        try {
            appDatabase.appDao().delete(id)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun read():Flow<List<SelectedAppInfoCore>>{
        return appDatabase.appDao().getAll().map { listOfEntities ->
            listOfEntities.map {
                SelectedAppInfoCore(
                    id = it.id,
                    name = it.name,
                    packageName = it.packageName
                )
            }
        }
    }

}