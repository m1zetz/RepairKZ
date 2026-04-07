package com.example.RepairKZ_Backend.model

import com.example.RepairKZ_Backend.common.enums.CitiesEnum
import com.example.RepairKZ_Backend.common.enums.MasterSpetializationsEnum
import com.example.RepairKZ_Backend.common.enums.StatusOfUser

data class MasterInfoDTO(
    val id: Long,
    val userPhotoUrl: String?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val status: StatusOfUser,
    val city: CitiesEnum,
    val experienceInYears: Int = 0,
    val description: String = "",
    val masterSpecialization: MasterSpetializationsEnum = MasterSpetializationsEnum.UNKNOWN,
)
