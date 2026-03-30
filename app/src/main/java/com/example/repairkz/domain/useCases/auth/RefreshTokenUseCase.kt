package com.example.repairkz.domain.useCases.auth

import com.example.repairkz.data.registration.RegistrationRepository
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository
) {
    suspend operator fun invoke() : Result<String>{
        return registrationRepository.refresh()
    }
}