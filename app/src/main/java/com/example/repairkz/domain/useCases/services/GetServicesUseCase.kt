package com.example.repairkz.domain.useCases.services

import com.example.repairkz.data.masterData.MasterRepository
import jakarta.inject.Inject

class GetServicesUseCase @Inject constructor(
    private val masterRepository: MasterRepository
) {
    operator fun invoke() = masterRepository.getServices()
}