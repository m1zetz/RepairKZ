package com.example.RepairKZ_Backend.model

data class UserRegistrationDTO(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val status: String,
    val city: String,
    val email: String,
    val password: String
)