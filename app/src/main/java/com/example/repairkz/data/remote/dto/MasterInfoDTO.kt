package com.example.repairkz.data.remote.dto

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser

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
