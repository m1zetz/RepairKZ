package com.example.repairkz.domain.useCases.searchData

import com.example.repairkz.common.models.Master
import com.example.repairkz.data.searchData.SearchRepository
import jakarta.inject.Inject

class GetMastersUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke() : List<Master>{
        return repository.getMasters()
    }
}