package com.erendogan6.dotoday.di

import com.erendogan6.dotoday.data.datasource.DoTodayDataSource
import com.erendogan6.dotoday.data.repo.DoTodayRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideDataSource():DoTodayDataSource{
        return DoTodayDataSource()
    }
}