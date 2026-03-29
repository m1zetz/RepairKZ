package com.example.repairkz.data.remote.dto

import com.example.repairkz.common.enums.StatusOfUser

data class ChangeStatusRequestDTO(
    val statusOfUser: StatusOfUser,
    val masterData: MasterRequestDTO? = null
)
