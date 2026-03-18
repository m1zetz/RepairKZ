package com.example.repairkz.data.registration

import com.example.repairkz.common.models.User
import com.example.repairkz.data.remote.api.RegistrationApi
import com.example.repairkz.data.remote.dto.CodeDTO
import com.example.repairkz.data.remote.dto.CreateUserDTO
import com.example.repairkz.data.remote.dto.EmailDTO
import com.example.repairkz.domain.repository.RegistrationRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    override suspend fun createUser(user: RequestBody, photo: MultipartBody.Part?): Result<Int> {
        return try{
            val response = registrationApi.createUser(user, photo)
            if (response.isSuccessful) Result.success(response.body()!!)
            else{
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