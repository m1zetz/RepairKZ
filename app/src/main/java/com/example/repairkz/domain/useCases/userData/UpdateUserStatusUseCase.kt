package com.example.repairkz.domain.useCases.userData

import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.data.remote.dto.order.ChangeStatusRequestDTO
import com.example.repairkz.data.remote.dto.FullUserRequestDTO
import com.example.repairkz.data.userData.UserRepository
import jakarta.inject.Inject

class UpdateUserStatusUseCase @Inject constructor(
    val repository: UserRepository
) {
    suspend operator fun invoke(id: Long, fullData: ChangeStatusRequestDTO){
        return repository.updateUserStatus(id,fullData)
    }
}