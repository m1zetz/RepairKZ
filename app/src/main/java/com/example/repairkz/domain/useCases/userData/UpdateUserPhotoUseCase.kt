package com.example.repairkz.domain.useCases.userData

import com.example.repairkz.common.models.User
import com.example.repairkz.data.remote.dto.UpdatePhotoResponseDTO
import com.example.repairkz.data.userData.UserRepository
import jakarta.inject.Inject
import okhttp3.MultipartBody

class UpdateUserPhotoUseCase@Inject constructor(
    val repository: UserRepository
) {
    suspend operator fun invoke(id: Long, file: MultipartBody.Part): Result<UpdatePhotoResponseDTO>{
        return repository.updateUserPhoto(id, file)
    }
}