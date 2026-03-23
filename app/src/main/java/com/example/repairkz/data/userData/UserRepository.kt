package com.example.repairkz.data.userData

import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import com.example.repairkz.data.local.entity.UserEntity
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val userData: StateFlow<User?>
    suspend fun fetchUserData() : User?
    suspend fun updateUserStatus(statusOfUser: StatusOfUser)

    suspend fun updateUserData(user: User)

    suspend fun getRoomData()


}