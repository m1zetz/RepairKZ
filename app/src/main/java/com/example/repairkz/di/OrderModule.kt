package com.example.repairkz.di

import com.example.repairkz.data.orderData.OrderRepository
import com.example.repairkz.data.orderData.OrderRepositoryImpl
import com.example.repairkz.data.remote.api.OrderApi
import com.example.repairkz.data.remote.api.RegistrationApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object OrderModule {
    @Provides
    fun provideOrderRepository(orderApi: OrderApi): OrderRepository {
        return OrderRepositoryImpl(orderApi)
    }

    @Provides
    fun providesOrderApi(retrofit: Retrofit): OrderApi {
        return retrofit.create(OrderApi::class.java)
    }
}