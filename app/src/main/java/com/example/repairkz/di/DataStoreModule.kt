package com.example.repairkz.di

import android.content.Context
import com.example.repairkz.data.local.dataStore.DataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DataStoreModule {
    @Provides
    fun provideDataStoreManger(@ApplicationContext context: Context) : DataStoreManager{
        return DataStoreManager(context)
    }
}