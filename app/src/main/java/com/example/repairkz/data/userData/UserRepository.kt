package com.example.repairkz.data.userData

import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val userData: StateFlow<User?>
    suspend fun fetchUserData() : Result<User>
    suspend fun updateUserStatus(statusOfUser: StatusOfUser)

    suspend fun updateUserData(user: User)


}