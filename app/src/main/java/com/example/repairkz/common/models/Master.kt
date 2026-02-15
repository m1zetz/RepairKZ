package com.example.repairkz.common.models

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser

data class Master(
    val id: Int,
    val masterSpecialization: MasterSpetializationsEnum,
    val masterName: String,
    val avatarURL: String? = null,
    val experienceInYears: Int,
    val description: String,
    val city: CitiesEnum,
    val statusOfUser: StatusOfUser
)
