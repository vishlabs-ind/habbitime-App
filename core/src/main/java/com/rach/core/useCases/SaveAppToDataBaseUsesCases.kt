package com.rach.core.useCases

import com.rach.core.data.model.SelectedAppInfoCore
import com.rach.core.domain.repo.SelectedAppDataSource
import javax.inject.Inject

class LoadAppUseCases @Inject constructor(
    private val selectedAppDataSource: SelectedAppDataSource
)  {
    fun invoke() = selectedAppDataSource.load()
}

class SaveAppUseCases @Inject constructor(
    private val selectedAppDataSource: SelectedAppDataSource
){
    suspend fun invoke(apps:List<SelectedAppInfoCore>){
        selectedAppDataSource.save(apps = apps)
    }
}

class DeleteAppUseCases @Inject constructor(
    private val selectedAppDataSource: SelectedAppDataSource
){
    suspend fun invoke(id:Int){
        selectedAppDataSource.delete(id)
    }
}