package com.erendogan6.dotoday.di

import android.content.Context
import androidx.room.Room
import com.erendogan6.dotoday.data.datasource.DoTodayDataSource
import com.erendogan6.dotoday.data.repo.DoTodayRepository
import com.erendogan6.dotoday.data.room.DoTodayDao
import com.erendogan6.dotoday.data.room.DoTodayDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideDoTodayRepository(dataSource: DoTodayDataSource): DoTodayRepository {
        return DoTodayRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideDataSource(doTodayDao: DoTodayDao): DoTodayDataSource {
        return DoTodayDataSource(doTodayDao = doTodayDao)
    }

    @Provides
    @Singleton
    fun provideDoTodayDao(@ApplicationContext context: Context): DoTodayDao{
        val db = Room.databaseBuilder(context, DoTodayDatabase::class.java,"do_today_App.db")
            .createFromAsset("do_today_App.db")
            .build()
        return db.getDao()
    }
}