package com.example.repairkz.data.remote.dto

import com.example.repairkz.common.enums.StatusOfUser

data class StatusRequestDTO(
    val statusOfUser: StatusOfUser,
    val masterData: MasterResponseDTO? = null
)
