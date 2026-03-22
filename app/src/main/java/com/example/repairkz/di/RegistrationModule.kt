package com.example.repairkz.di

import com.example.repairkz.data.remote.api.RegistrationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object RegistrationModule{
    @Provides
    fun providesRegistration(retrofit: Retrofit): RegistrationApi {
        return retrofit.create(RegistrationApi::class.java)
    }
}