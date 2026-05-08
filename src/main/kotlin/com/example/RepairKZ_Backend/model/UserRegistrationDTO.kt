package com.example.RepairKZ_Backend.model

import com.example.RepairKZ_Backend.common.enums.CitiesEnum
import com.example.RepairKZ_Backend.common.enums.StatusOfUser

data class UserRegistrationDTO(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val status: StatusOfUser,
    val city: CitiesEnum?,
    val email: String,
    val password: String
)