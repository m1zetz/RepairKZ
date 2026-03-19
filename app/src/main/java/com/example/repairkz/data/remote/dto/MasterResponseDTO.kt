package com.example.repairkz.data.remote.dto

import com.example.repairkz.common.enums.MasterSpetializationsEnum

data class MasterResponseDTO(
    val experienceInYears: Int? = null,
    val description: String? = null,
    val masterSpecialization: MasterSpetializationsEnum? = null
)
