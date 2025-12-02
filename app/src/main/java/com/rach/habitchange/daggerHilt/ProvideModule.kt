package com.rach.habitchange.daggerHilt

import android.content.Context
import com.rach.core.data.repoImp.SelectAppRepoImp
import com.rach.core.data.repoImp.UsageStatsRepositoryImpl
import com.rach.core.domain.repo.AppRepository
import com.rach.core.domain.repo.UsageStatsRepository
import com.rach.habitchange.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvideModule {

    // jb ek repoImp me multiple constructor pass ho toh best apka provide hota hai
    // urna apka binds is best

    /*
     If a repository implementation has multiple constructors, then @Provides is the best way to handle its injection — otherwise,
     if there’s only one constructor with @Inject, then @Binds is the better and cleaner option.

     short :
      Multiple constructors ➜ @Provides is best.
      Single @Inject constructor ➜ @Binds is best.

     */
    @Provides
    @Singleton
    fun provideSelectAppProvide(@ApplicationContext context: Context): AppRepository {
        return SelectAppRepoImp(context)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideUsageStatsRepository(@ApplicationContext context: Context):UsageStatsRepository{
        return UsageStatsRepositoryImpl(context)
    }


}