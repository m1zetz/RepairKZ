package com.example.repairkz.domain.repository

import com.example.repairkz.data.remote.api.RegistrationApi
import com.example.repairkz.data.remote.dto.EmailDTO
import retrofit2.Response
import javax.inject.Inject

interface RegistrationRepository {
    suspend fun getCode(email: String): Result<Unit>
    suspend fun sendCode(code: Int, email: String) : Result<Unit>
}
