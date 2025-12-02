package com.rach.core.useCases

import com.rach.core.data.model.SelectedAppInfoCore
import com.rach.core.domain.repo.AppRepository
import javax.inject.Inject

class SelectAppUseCases @Inject constructor(
    private val repository: AppRepository
) {
    suspend fun getAllInstallApps():Result<List<SelectedAppInfoCore>>{
        return repository.getAllInstalledApps()
    }
}