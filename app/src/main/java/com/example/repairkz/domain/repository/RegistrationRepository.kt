package com.example.repairkz.domain.repository

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface RegistrationRepository {
    suspend fun getCode(email: String): Result<Unit>
    suspend fun sendCode(code: Int, email: String) : Result<Unit>
    suspend fun createUser(user: RequestBody, photo: MultipartBody.Part?) : Result<Int>
}
