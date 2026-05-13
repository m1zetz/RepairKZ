package com.example.repairkz.di

import com.example.repairkz.data.remote.api.MasterApi
import com.example.repairkz.data.remote.api.ServicesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object ServiceModule {
    @Provides
    fun providesMaster(retrofit: Retrofit) : ServicesApi{
        return retrofit.create(ServicesApi::class.java)
    }

}