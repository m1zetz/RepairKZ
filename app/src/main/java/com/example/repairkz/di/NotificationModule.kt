package com.example.repairkz.di

import com.example.repairkz.data.remote.api.FcmApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object NotificationModule {
    @Provides
    fun provideFcmApi(retrofit: Retrofit): FcmApi {
        return retrofit.create(FcmApi::class.java)
    }
}