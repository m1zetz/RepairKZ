package com.example.repairkz.data.remote.api

import com.example.repairkz.common.models.User
import com.example.repairkz.data.remote.dto.CodeDTO
import com.example.repairkz.data.remote.dto.CreateUserDTO
import com.example.repairkz.data.remote.dto.EmailDTO
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RegistrationApi {
    @POST("api/users/get-code")
    suspend fun getCode(@Body emailDTO: EmailDTO) : Response<Unit>

    @POST("api/users/check-code")
    suspend fun sendCode(@Body codeDTO: CodeDTO) : Response<Unit>

    @Multipart
    @POST("api/users/create-user")
    suspend fun createUser(
        @Part("user") user: RequestBody,
        @Part photo: MultipartBody.Part?
        ) : Response<Int>
}