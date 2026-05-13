package com.example.repairkz.domain.useCases.services

import com.example.repairkz.data.masterData.MasterRepository
import com.example.repairkz.data.remote.dto.MasterServiceDTO
import jakarta.inject.Inject

class CreateServiceUseCase @Inject constructor(
    private val masterRepository: MasterRepository
) {
    suspend operator fun invoke(dto: MasterServiceDTO) = masterRepository.createService(dto)
}