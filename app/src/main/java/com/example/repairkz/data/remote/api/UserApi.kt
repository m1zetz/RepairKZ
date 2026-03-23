package com.example.repairkz.data.remote.api

import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    @PUT("api/users/change-status/{id}")
    suspend fun updateStatus(
        @Path("id") id: Int,
        @Body statusOfUser: StatusOfUser,
    )
}