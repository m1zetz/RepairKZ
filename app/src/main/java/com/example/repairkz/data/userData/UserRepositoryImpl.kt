package com.example.repairkz.data.userData

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    private val _userData = MutableStateFlow<User?>(null)
    override val userData = _userData.asStateFlow()

    override suspend fun fetchUserData(): User? {
        val currentState = userData.value
        return currentState
    }

    override suspend fun updateUserData(user: User) {
        _userData.value = user
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

}