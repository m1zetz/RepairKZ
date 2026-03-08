package com.example.repairkz.domain.useCases.registration

import com.example.repairkz.data.repository.RegistrationRepositoryImpl
import jakarta.inject.Inject

class SendCodeUseCase @Inject constructor(
    private val registrationRepositoryImpl: RegistrationRepositoryImpl
) {
    suspend operator fun invoke(code: Int, email: String) = registrationRepositoryImpl.sendCode(code, email)
}