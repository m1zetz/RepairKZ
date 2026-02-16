package com.example.repairkz.domain.useCases.userData

import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.data.userData.UserRepository
import jakarta.inject.Inject

class UpdateUserStatusUseCase @Inject constructor(
    val repository: UserRepository
) {

    suspend operator fun invoke(status: StatusOfUser){
        return repository.updateUserStatus(status)
    }
}