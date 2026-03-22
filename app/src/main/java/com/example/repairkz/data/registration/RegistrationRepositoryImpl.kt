package com.example.repairkz.data.registration

import android.util.Log
import com.example.repairkz.data.remote.api.RegistrationApi
import com.example.repairkz.data.remote.api.TokenApi
import com.example.repairkz.data.remote.dto.CodeDTO
import com.example.repairkz.data.remote.dto.EmailDTO
import com.example.repairkz.data.remote.dto.LoginDTO
import com.example.repairkz.data.remote.dto.LoginResponseDTO
import com.example.repairkz.data.remote.dto.RefreshResponseDTO
import com.example.repairkz.data.remote.dto.RegistrationResponseDTO
import com.example.repairkz.domain.repository.RegistrationRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class RegistrationRepositoryImpl @Inject constructor(
    private val registrationApi: RegistrationApi,
    private val tokenApi: TokenApi
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

    override suspend fun registration(user: RequestBody, photo: MultipartBody.Part?): Result<RegistrationResponseDTO> {
        return try{
            val response = registrationApi.register(user, photo)
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

    override suspend fun login(loginDTO: LoginDTO): Result<LoginResponseDTO> {
        return try{
            val response = registrationApi.login(loginDTO)
            if(response.isSuccessful) Result.success(response.body()!!)
            else{
                val errorMsg = when(response.code()){
                    400 -> "wrong data"
                    else -> "network error"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun refresh(): Result<String> {
        return try{
            val response = tokenApi.refresh()
            if (response.isSuccessful) Result.success(response.body()!!.token)
            else{
                Log.e("Refresh", "Code: ${response.code()} Body: ${response.errorBody()?.string()}")

                val errorMsg = "error"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception){
            Log.e("Refresh", "Exception: ${e::class.simpleName} ${e.message}", e)

            Result.failure(e)
        }

    }
}