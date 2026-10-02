package com.example.repairkz.domain.useCases.fcm

import com.example.repairkz.data.registration.RegistrationRepository
import javax.inject.Inject

class RegisterFcmTokenUseCase @Inject constructor(
    private val registrationRepository: RegistrationRepository
) {
    suspend operator fun invoke(userId: Long, token: String): Result<Unit> {
        return registrationRepository.registerFcmToken(userId, token)
    }
}