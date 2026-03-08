package com.example.repairkz.data.remote.api

import com.example.repairkz.data.remote.dto.CodeDTO
import com.example.repairkz.data.remote.dto.EmailDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationApi {
    @POST("api/users/get-code")
    suspend fun getCode(@Body emailDTO: EmailDTO) : Response<Unit>

    @POST("api/users/check-code")
    suspend fun sendCode(@Body codeDTO: CodeDTO) : Response<Unit>
}