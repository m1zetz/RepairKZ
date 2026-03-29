package com.example.RepairKZ_Backend.model


data class LoginResponseDTO(
    val id: Long,
    val token: String,
    val user: UserResponseDTO,
    val master: MasterShortInfoDTO? = null
)
