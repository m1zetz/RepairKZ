package com.example.repairkz.data.remote.dto

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.StatusOfUser

data class CreateUserDTO(
    val firstName: String,
    val lastName: String,
    val userPhotoUrl: String?,
    val email: String,
    val password: String?,
    val phone: String,
    val status: StatusOfUser,
    val city: CitiesEnum
)