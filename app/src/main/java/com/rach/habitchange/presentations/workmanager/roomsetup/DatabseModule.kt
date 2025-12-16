package com.rach.habitchange.presentations.workmanager.roomsetup

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import com.rach.habitchange.presentations.workmanager.roomsetup.Appdatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context : Context): Appdatabase{
        return Room.databaseBuilder(
            context.applicationContext,
            Appdatabase::class.java,
            "a"
        ).build()
    }
    @Provides
    @Singleton
    fun provideLimitDao(
        database: Appdatabase
    ): Idao {
        return database.daolimit()
    }
}