package com.example.repairkz.data.remote.dto

import com.example.repairkz.common.enums.MasterSpetializationsEnum

data class MasterResponseDTO(
    val experienceInYears: Int = 0,
    val description: String = "",
    val masterSpecialization: MasterSpetializationsEnum = MasterSpetializationsEnum.UNKNOWN
)
