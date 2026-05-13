package com.example.repairkz.domain.useCases.userData

import com.example.repairkz.common.models.User
import com.example.repairkz.data.userData.UserRepository
import com.example.repairkz.data.userData.UserRepositoryImpl
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetUserDataUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() : Flow<User?> {
        return repository.userData
    }
}