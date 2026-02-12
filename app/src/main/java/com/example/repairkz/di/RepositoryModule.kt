package com.example.repairkz.di

import com.example.repairkz.data.masterData.MasterRepository
import com.example.repairkz.data.masterData.MasterRepositoryImpl
import com.example.repairkz.data.notificationData.NotificationRepository
import com.example.repairkz.data.notificationData.NotificationRepositoryImpl
import com.example.repairkz.data.searchData.SearchRepository
import com.example.repairkz.data.searchData.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule{
    @Provides
    fun provideNotificationRepository() : NotificationRepository{
        return NotificationRepositoryImpl()
    }

    @Provides
    fun provideSearchRepository() : SearchRepository {
        return SearchRepositoryImpl()
    }

    @Provides
    fun provideMasterRepository() : MasterRepository {
        return MasterRepositoryImpl()
    }
}