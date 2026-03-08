package com.example.repairkz.data.repository

import com.example.repairkz.R

import com.example.repairkz.data.remote.api.RegistrationApi
import com.example.repairkz.data.remote.dto.CodeDTO
import com.example.repairkz.data.remote.dto.EmailDTO
import com.example.repairkz.domain.repository.RegistrationRepository
import retrofit2.Response
import javax.inject.Inject

class RegistrationRepositoryImpl @Inject constructor(
    private val registrationApi: RegistrationApi
) : RegistrationRepository{
    override suspend fun getCode(email: String) : Result<Unit>{
        return try {
            val response = registrationApi.getCode(EmailDTO(email))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Ошибка сервера: ${response.code()}"))
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun sendCode(code: Int, email: String): Result<Unit> {
        return try {
            val response = registrationApi.sendCode(CodeDTO(code, email))
            if (response.isSuccessful) Result.success(Unit)
            else {
                val errorMsg = when(response.code()){
                    400 -> "wrong_code"
                    else -> "network_error"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}