package com.example.repairkz.data.userData

import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    private val _userData = MutableStateFlow<User?>(null)
    override val userData = _userData.asStateFlow()

    override suspend fun fetchUserData(): Result<User> {
        val user = User(
            1,
            null,
            "Maxim",
            "Ius",
            "iusmaxim@gmail.com",
            StatusOfUser.CLIENT
        )
        _userData.value = null
        return Result.success(user)
    }
}