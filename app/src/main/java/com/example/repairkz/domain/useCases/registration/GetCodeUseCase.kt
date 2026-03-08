package com.example.repairkz.domain.useCases.registration

import com.example.repairkz.data.remote.api.RegistrationApi
import com.example.repairkz.domain.repository.RegistrationRepository
import jakarta.inject.Inject

class GetCodeUseCase @Inject constructor(
    private val repository: RegistrationRepository
) {
    suspend operator fun invoke(email: String) = repository.getCode(email)
}