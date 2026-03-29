package com.example.repairkz.domain.useCases.userData

import com.example.repairkz.common.models.User
import com.example.repairkz.data.userData.UserRepository
import jakarta.inject.Inject

class SaveUserToLocalUseCase@Inject constructor(
    val repository: UserRepository
) {
    suspend operator fun invoke(user: User){
        return repository.saveUserToLocal(user)
    }
}