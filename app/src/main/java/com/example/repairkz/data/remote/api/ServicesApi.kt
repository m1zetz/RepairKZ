package com.example.repairkz.data.remote.api

import com.example.repairkz.data.remote.dto.MasterResponseDTO
import com.example.repairkz.data.remote.dto.MasterServiceDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServicesApi {
    @POST("api/master/create")
    suspend fun createService(@Body masterServiceDto: MasterServiceDTO) : Response<MasterServiceDTO>
    @PUT("api/master/update")
    suspend fun updateService(@Body masterServiceDto: MasterServiceDTO) : Response<MasterServiceDTO>
    @DELETE("api/master/delete/{id}")
    suspend fun deleteService(@Path("id") id: Long) : Response<Unit>
}