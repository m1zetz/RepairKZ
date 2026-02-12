package com.example.repairkz.domain.useCases.masterData

import com.example.repairkz.common.models.Master
import com.example.repairkz.data.masterData.MasterRepository
import jakarta.inject.Inject

class GetMasterByIdUseCase @Inject constructor(
    private val masterRepository: MasterRepository
) {
    suspend operator fun invoke(id: Int) : Result<Master> {
        return masterRepository.fetchMasterById(id)
    }
}