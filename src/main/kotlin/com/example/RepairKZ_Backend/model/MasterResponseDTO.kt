package com.example.RepairKZ_Backend.model

import com.example.RepairKZ_Backend.common.enums.MasterSpetializationsEnum

data class MasterResponseDTO(
    val user: UserResponseDTO,
    val experienceInYears: Int? = null,
    val description: String? = null,
    val masterSpecialization: MasterSpetializationsEnum? = null
)
