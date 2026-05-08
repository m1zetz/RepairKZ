package com.example.repairkz.di

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.repairkz.common.constants.SERVER_IP
import com.example.repairkz.data.local.dataStore.DataStoreManager
import com.example.repairkz.data.remote.api.RegistrationApi
import com.google.gson.GsonBuilder
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
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
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://repairkz-copy-production.up.railway.app/"

    @RequiresApi(Build.VERSION_CODES.O)
    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java,
                JsonSerializer<LocalDateTime> { src, _, _ ->
                    JsonPrimitive(src.toString())
                })
            .create()
        return Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build()

    }

    @Provides
    fun okHttpInterceptor(
        dataStoreManager: DataStoreManager,
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor { Log.d("OkHttp", it) }
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient()
            .newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(logging)
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