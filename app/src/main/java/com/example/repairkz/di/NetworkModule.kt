package com.example.repairkz.di

import com.example.repairkz.data.remote.api.RegistrationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://192.168.0.4:8080/"

    @Provides
    fun providesRetrofit() : Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Provides
    fun providesRegistration(retrofit: Retrofit) : RegistrationApi{
        return retrofit.create(RegistrationApi::class.java)
    }

}