package com.example.repairkz.data.userData

import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import com.example.repairkz.data.local.entity.UserEntity
import com.example.repairkz.data.remote.dto.order.ChangeStatusRequestDTO
import com.example.repairkz.data.remote.dto.UpdatePhotoResponseDTO
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MultipartBody
import retrofit2.http.Multipart

interface UserRepository {
    val userData: StateFlow<User?>
    suspend fun fetchUserData() : User?
    suspend fun saveUserToLocal(user:User)
    suspend fun updateUserStatus(id: Long, dto: ChangeStatusRequestDTO)

    suspend fun updateUserData(user: User)
    suspend fun updateUserPhoto(id: Long ,file: MultipartBody.Part) : Result<UpdatePhotoResponseDTO>

    suspend fun getRoomData()


}