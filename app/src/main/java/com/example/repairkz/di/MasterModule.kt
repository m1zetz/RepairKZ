package com.example.repairkz.di

import com.example.repairkz.data.remote.api.MasterApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create

@InstallIn(SingletonComponent::class)
@Module
object MasterModule {
    @Provides
    fun providesMaster(retrofit: Retrofit) : MasterApi{
        return retrofit.create(MasterApi::class.java)
    }
}