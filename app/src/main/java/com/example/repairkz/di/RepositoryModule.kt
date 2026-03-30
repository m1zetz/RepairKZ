package com.example.repairkz.di

import android.content.Context
import com.example.repairkz.data.fileData.FileRepository
import com.example.repairkz.data.fileData.FileRepositoryImpl
import com.example.repairkz.data.masterData.MasterRepository
import com.example.repairkz.data.masterData.MasterRepositoryImpl
import com.example.repairkz.data.notificationData.NotificationRepository
import com.example.repairkz.data.notificationData.NotificationRepositoryImpl
import com.example.repairkz.data.registration.RegistrationRepositoryImpl
import com.example.repairkz.data.remote.api.RegistrationApi
import com.example.repairkz.data.remote.api.TokenApi
import com.example.repairkz.data.remote.api.UserApi
import com.example.repairkz.data.registration.RegistrationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule{

    @Provides
    fun provideNotificationRepository() : NotificationRepository{
        return NotificationRepositoryImpl()
    }
    @Provides
    fun provideMasterRepository() : MasterRepository {
        return MasterRepositoryImpl()
    }

    @Provides
    fun provideFileRepository(@ApplicationContext context: Context) : FileRepository {
        return FileRepositoryImpl(context)
    }


    @Provides
    fun provideRegistrationRepository(registrationApi: RegistrationApi, tokenApi: TokenApi, userApi: UserApi) : RegistrationRepository{
        return RegistrationRepositoryImpl(
            registrationApi = registrationApi,
            tokenApi = tokenApi,
            userApi = userApi
        )
    }

    

}