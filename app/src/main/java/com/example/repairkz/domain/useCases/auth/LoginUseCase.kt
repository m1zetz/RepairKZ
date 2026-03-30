package com.example.repairkz.domain.useCases.auth

import com.example.repairkz.data.remote.dto.LoginDTO
import com.example.repairkz.data.remote.dto.LoginResponseDTO
import com.example.repairkz.data.registration.RegistrationRepository
import jakarta.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: RegistrationRepository
) {
    suspend operator fun invoke(loginDTO: LoginDTO) : Result<LoginResponseDTO>{
        return repository.login(loginDTO)
    }
}