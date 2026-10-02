package com.example.repairkz.data.remote.dto.FCM

data class RegisterTokenRequestDTO(
    val userId: Long,
    val token: String
)