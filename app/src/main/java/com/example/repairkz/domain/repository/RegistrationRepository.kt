package com.example.repairkz.domain.repository

import com.example.repairkz.data.remote.dto.LoginDTO
import com.example.repairkz.data.remote.dto.LoginResponseDTO
import com.example.repairkz.data.remote.dto.RegistrationResponseDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface RegistrationRepository {
    suspend fun getCode(email: String): Result<Unit>
    suspend fun sendCode(code: Int, email: String) : Result<Unit>
    suspend fun registration(user: RequestBody, photo: MultipartBody.Part?) : Result<RegistrationResponseDTO>
    suspend fun login(loginDTO: LoginDTO) : Result<LoginResponseDTO>
}
