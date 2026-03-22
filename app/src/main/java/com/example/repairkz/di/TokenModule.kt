package com.example.repairkz.di

import com.example.repairkz.data.remote.api.TokenApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object TokenModule{
    @Provides
    fun provideTokenApi(retrofit: Retrofit) : TokenApi{
        return retrofit.create(TokenApi::class.java)
    }
}