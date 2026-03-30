package com.example.repairkz.data.registration

import com.example.repairkz.data.remote.dto.LoginDTO
import com.example.repairkz.data.remote.dto.LoginResponseDTO
import com.example.repairkz.data.remote.dto.RegistrationResponseDTO
import com.example.repairkz.data.remote.dto.UserRegistrationDTO
import okhttp3.MultipartBody

interface RegistrationRepository {
    suspend fun getCode(email: String): Result<Unit>
    suspend fun sendCode(code: Int, email: String) : Result<Unit>
    suspend fun registration(user: UserRegistrationDTO, photo: MultipartBody.Part?) : Result<RegistrationResponseDTO>
    suspend fun login(loginDTO: LoginDTO) : Result<LoginResponseDTO>

    suspend fun refresh() : Result<String>
}