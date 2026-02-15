package com.example.repairkz.domain.useCases.masterData

import com.example.repairkz.common.models.Master
import com.example.repairkz.data.masterData.MasterRepository
import jakarta.inject.Inject

class GetMastersUseCase @Inject constructor(
    private val repository: MasterRepository
) {
    suspend operator fun invoke() : Result<List<Master>>{
        return repository.getMasters()
    }
}