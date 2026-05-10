package com.example.repairkz.data.remote.api

import com.example.repairkz.common.models.Master
import com.example.repairkz.data.remote.dto.MasterInfoDTO
import com.example.repairkz.data.remote.dto.MasterResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MasterApi {
    @GET("api/users/get-masters/{currentId}")
    suspend fun getMasters(@Path("currentId") currentId: Long) : Response<List<MasterInfoDTO>>

    @GET("/api/users/get-master-by-id/{id}")
    suspend fun getMasterById(@Path("id") id: Long) : Response<MasterResponseDTO>
}