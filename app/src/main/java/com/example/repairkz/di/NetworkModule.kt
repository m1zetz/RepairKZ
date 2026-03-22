package com.example.repairkz.di

import com.example.repairkz.data.local.dataStore.DataStoreManager
import com.example.repairkz.data.remote.api.RegistrationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://192.168.0.4:8080/"

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    }

    @Provides
    fun okHttpInterceptor(
        dataStoreManager: DataStoreManager,
    ): OkHttpClient {

        return OkHttpClient().newBuilder()
            .addInterceptor {chain ->
                val token = runBlocking {
                    dataStoreManager.tokenFlow.first()
                }
                val request = if(token != "-1" && token.isNotEmpty()){
                    chain.request()
                        .newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                }
                else{
                    chain.request()
                }
                chain.proceed(request)
            }.build()
    }



}