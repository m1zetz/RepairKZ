package com.example.repairkz.domain.useCases.services

import com.example.repairkz.data.masterData.MasterRepository
import jakarta.inject.Inject

class DeleteServiceUseCase @Inject constructor(
    private val masterRepository: MasterRepository,
) {
    suspend operator fun invoke(id: Long) = masterRepository.deleteService(id)
}