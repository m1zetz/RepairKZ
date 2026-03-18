package com.example.repairkz.domain.useCases.registration

import com.example.repairkz.domain.repository.RegistrationRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(
    private val repository: RegistrationRepository
) {
    suspend operator fun invoke(user: RequestBody, photo: MultipartBody.Part?) : Result<Int>{
        return repository.createUser(user, photo)
    }
}