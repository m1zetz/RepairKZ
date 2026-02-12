package com.example.repairkz.domain.useCases.userData

import com.example.repairkz.common.models.User
import com.example.repairkz.data.userData.UserRepository
import com.example.repairkz.data.userData.UserRepositoryImpl
import jakarta.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() : Result<User> {
        return repository.fetchUserData()
    }
}