package com.rach.core.domain.repo

import com.rach.core.data.model.SelectedAppInfoCore

interface AppRepository {
    suspend fun getAllInstalledApps():Result<List<SelectedAppInfoCore>>
}

