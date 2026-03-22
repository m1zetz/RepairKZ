package com.example.repairkz.data.remote.api

import com.example.repairkz.data.remote.dto.RefreshResponseDTO
import retrofit2.Response
import retrofit2.http.GET


interface TokenApi {
    @GET("api/refresh-token")
    suspend fun refresh() : Response<RefreshResponseDTO>
}