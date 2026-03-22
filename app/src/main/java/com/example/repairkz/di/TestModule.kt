package com.example.repairkz.di

import com.example.repairkz.data.remote.api.TestApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object TestModule{
    @Provides
    fun provideTestApi(retrofit: Retrofit) : TestApi{
        return retrofit.create(TestApi::class.java)
    }
}