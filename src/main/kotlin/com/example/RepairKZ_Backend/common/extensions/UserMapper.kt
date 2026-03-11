package com.example.RepairKZ_Backend.common.extensions

import com.example.RepairKZ_Backend.entity.User
import com.example.RepairKZ_Backend.model.UserRegistrationDTO
import com.example.RepairKZ_Backend.model.UserResponseDTO

fun User.toDTO(): UserRegistrationDTO {
    return UserRegistrationDTO(
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        phone = this.phoneNumber,
        status = this.status,
        city = this.city,
        password = this.password,
    )
}

fun User.toResponseDTO(): UserResponseDTO {
    return UserResponseDTO(
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        phone = this.phoneNumber,
        status = this.status,
        city = this.city,
    )
}