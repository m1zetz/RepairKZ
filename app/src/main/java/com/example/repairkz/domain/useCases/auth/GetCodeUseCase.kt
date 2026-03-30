package com.example.repairkz.domain.useCases.auth

import com.example.repairkz.data.registration.RegistrationRepository
import jakarta.inject.Inject

class GetCodeUseCase @Inject constructor(
    private val repository: RegistrationRepository
) {
    suspend operator fun invoke(email: String) = repository.getCode(email)
}