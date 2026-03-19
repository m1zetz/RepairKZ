package com.example.repairkz.data.remote.dto

data class LoginResponseDTO(
    val id: Long,
    val token: String,
    val user: UserResponseDTO,
    val master: MasterResponseDTO? = null
)
