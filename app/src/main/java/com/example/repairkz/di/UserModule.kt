package com.example.repairkz.di

import com.example.repairkz.data.remote.api.UserApi
import com.example.repairkz.data.userData.UserRepository
import com.example.repairkz.data.userData.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule{
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ) : UserRepository

    companion object{
        @Provides
        fun provideUserApi(retrofit: Retrofit) : UserApi{
            return retrofit.create(UserApi::class.java)
        }
    }

}