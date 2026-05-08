package com.example.repairkz.data.remote.dto

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.StatusOfUser

data class UserResponseDTO(
    val id: Long,
    val userPhotoUrl: String?,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val status: StatusOfUser,
    val city: CitiesEnum?,
    val email: String,
)
