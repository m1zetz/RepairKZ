package com.example.repairkz.data.remote.dto

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser

data class MasterInfoDTO(
    val id: Long,
    val userPhotoUrl: String?,
    val firstName: String,
    val lastName: String,
    val masterSpecialization: MasterSpetializationsEnum = MasterSpetializationsEnum.UNKNOWN,
)
