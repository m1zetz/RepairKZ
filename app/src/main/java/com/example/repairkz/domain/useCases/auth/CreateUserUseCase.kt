package com.example.repairkz.domain.useCases.auth

import com.example.repairkz.data.remote.dto.RegistrationResponseDTO
import com.example.repairkz.domain.repository.RegistrationRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: RegistrationRepository
) {
    suspend operator fun invoke(user: RequestBody, photo: MultipartBody.Part?) : Result<RegistrationResponseDTO>{
        return repository.registration(user, photo)
    }
}