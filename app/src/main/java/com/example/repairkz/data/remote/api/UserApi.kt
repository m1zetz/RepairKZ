package com.example.repairkz.data.remote.api

import com.example.repairkz.data.remote.dto.order.ChangeStatusRequestDTO
import com.example.repairkz.data.remote.dto.FullUserRequestDTO
import com.example.repairkz.data.remote.dto.MasterDataResponseDTO
import com.example.repairkz.data.remote.dto.MasterResponseDTO
import com.example.repairkz.data.remote.dto.StatusRequestDTO
import com.example.repairkz.data.remote.dto.UpdatePhotoResponseDTO
import com.example.repairkz.data.remote.dto.UserResponseDTO
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {

    @Multipart
    @PUT("api/users/update-photo/{id}")
    suspend fun updateUserPhoto(
        @Path("id") id: Long,
        @Part file: MultipartBody.Part
    ) : Response<UpdatePhotoResponseDTO>
    @PUT("api/users/update-user")
    suspend fun updateUser(
        @Body fullUser: FullUserRequestDTO,
    ) : Response<FullUserRequestDTO>

    @PUT("api/users/change-status/{id}")
    suspend fun updateStatus(
        @Path("id") id: Long,
        @Body dto: ChangeStatusRequestDTO,
    ) : Response<MasterResponseDTO>
}