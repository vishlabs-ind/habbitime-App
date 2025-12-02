package com.rach.habitchange.daggerHilt

import com.rach.core.data.repoImp.UsageStatsRepositoryImpl
import com.rach.core.domain.repo.SelectedAppDataSource
import com.rach.core.domain.repo.UsageStatsRepository
import com.rach.habitchange.db.apps.AppDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {

    // binds module is best for single constructor that pass in repoImp
    // esme apko repoImp () esme pass krna hota hai

    @Binds
    @Singleton
    abstract fun bindsAppDataSource(impl : AppDataSource): SelectedAppDataSource



}