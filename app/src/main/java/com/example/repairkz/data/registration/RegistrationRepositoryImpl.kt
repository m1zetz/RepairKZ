package com.example.repairkz.data.registration

import android.util.Log
import com.example.repairkz.data.remote.api.RegistrationApi
import com.example.repairkz.data.remote.api.TokenApi
import com.example.repairkz.data.remote.api.UserApi
import com.example.repairkz.data.remote.dto.CodeDTO
import com.example.repairkz.data.remote.dto.UserRegistrationDTO
import com.example.repairkz.data.remote.dto.EmailDTO
import com.example.repairkz.data.remote.dto.LoginDTO
import com.example.repairkz.data.remote.dto.LoginResponseDTO
import com.example.repairkz.data.remote.dto.RegistrationResponseDTO
import com.example.repairkz.data.registration.RegistrationRepository
import com.example.repairkz.domain.errors.AuthorizationError
import okhttp3.MultipartBody
import javax.inject.Inject

class RegistrationRepositoryImpl @Inject constructor(
    private val registrationApi: RegistrationApi,
    private val tokenApi: TokenApi,
    private val userApi: UserApi,
) : RegistrationRepository {
    override suspend fun getCode(email: String): Result<Unit> {
        return try {
            val response = registrationApi.getCode(EmailDTO(email))
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Ошибка сервера: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendCode(code: Int, email: String): Result<Unit> {
        return try {
            val response = registrationApi.sendCode(CodeDTO(code, email))
            if (response.isSuccessful) Result.success(Unit)
            else {
                val errorMsg = when (response.code()) {
                    400 -> "wrong_code"
                    else -> "network_error"
                }
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registration(
        user: UserRegistrationDTO,
        photo: MultipartBody.Part?,
    ): Result<RegistrationResponseDTO> {
        return try {
            val response = registrationApi.register(user)
            if (!response.isSuccessful) {
                val errorMsg = if (response.code() == 400) "wrong_code" else "network_error"
                return Result.failure(Exception(errorMsg))
            }
            val userData = response.body() ?: return Result.failure(Exception("Empty response"))
            if (photo != null) {
                val photoResponse = userApi.updateUserPhoto(userData.id, photo)
                if (photoResponse.isSuccessful) {
                    val newPhotoUrl = photoResponse.body()?.photoUrl
                    return Result.success(userData.copy(photoUrl = newPhotoUrl))
                } else {
                    return Result.success(userData)
                }
            }
            Result.success(userData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(loginDTO: LoginDTO): Result<LoginResponseDTO> {
        return try {
            val response = registrationApi.login(loginDTO)
            if (response.isSuccessful) Result.success(response.body()!!)
            else {
                Result.failure(
                    when (response.code()) {
                        400 -> AuthorizationError.WrongLoginOrPassword
                        else -> AuthorizationError.NetworkError
                    }
                )
            }
        } catch (e: Exception) {
            Result.failure(AuthorizationError.NoInternet)
        }
    }

    override suspend fun refresh(): Result<String> {
        return try {
            val response = tokenApi.refresh()
            if (response.isSuccessful) Result.success(response.body()!!.token)
            else {
                Result.failure(
                    when(response.code()){
                        401 -> AuthorizationError.TokenExpired
                        else -> AuthorizationError.NetworkError
                    }
                )
            }
        } catch (e: Exception) {
            Result.failure(AuthorizationError.NoInternet)
        }

    }
}