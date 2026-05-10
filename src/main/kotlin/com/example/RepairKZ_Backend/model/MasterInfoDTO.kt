package com.example.RepairKZ_Backend.model

import com.example.RepairKZ_Backend.common.enums.CitiesEnum
import com.example.RepairKZ_Backend.common.enums.MasterSpetializationsEnum
import com.example.RepairKZ_Backend.common.enums.StatusOfUser

data class MasterInfoDTO(
    val id: Long,
    val userPhotoUrl: String?,
    val firstName: String,
    val lastName: String,
    val masterSpecialization: MasterSpetializationsEnum = MasterSpetializationsEnum.UNKNOWN,
)
