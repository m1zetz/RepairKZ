package com.example.repairkz.data.remote.api

import com.example.repairkz.data.remote.dto.FCM.RegisterTokenRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    @POST("api/fcm/register")
    suspend fun registerToken(@Body request: RegisterTokenRequestDTO): Response<Unit>
}