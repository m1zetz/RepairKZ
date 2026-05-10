package com.example.RepairKZ_Backend.model

import com.example.RepairKZ_Backend.common.enums.MasterSpetializationsEnum

data class MasterShortInfoDTO(
    val experienceInYears: Int?,
    val description: String?,
    val masterSpecialization: MasterSpetializationsEnum?,
    val services: List<MasterServiceDTO>? = null
)
