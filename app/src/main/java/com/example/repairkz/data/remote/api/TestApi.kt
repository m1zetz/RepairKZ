package com.example.repairkz.data.remote.api

import retrofit2.Response
import retrofit2.http.GET


interface TestApi {
    @GET("/hello")
    suspend fun hello() : Response<String>
}