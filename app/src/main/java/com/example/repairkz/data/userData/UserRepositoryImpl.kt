package com.example.repairkz.data.userData

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    private val _userData = MutableStateFlow<User?>(null)
    override val userData = _userData.asStateFlow()

    override suspend fun fetchUserData(): Result<User> {
        val currentState = userData.value
        if (currentState != null) {
            return Result.success(currentState)
        } else {
            val user = User(
                userId = 1,
                userPhotoUrl = null,
                firstName = "Maxim",
                lastName = "Ius",
                email = "iusmaxim@gmail.com",
                phoneNumber = "+77071234567",
                status = StatusOfUser.CLIENT,
                city = CitiesEnum.ALMATY
            )
            _userData.value = user
            return Result.success(user)
        }
    }

    override suspend fun updateUserStatus(statusOfUser: StatusOfUser) {
        when (statusOfUser) {
            StatusOfUser.CLIENT -> {
                _userData.update {
                    it?.toUser()
                }
            }

            StatusOfUser.MASTER -> {
                _userData.update {
                    it?.toMaster()
                }
            }
        }
    }

    override suspend fun updateUserData(user: User) {
        _userData.value = user
    }
}