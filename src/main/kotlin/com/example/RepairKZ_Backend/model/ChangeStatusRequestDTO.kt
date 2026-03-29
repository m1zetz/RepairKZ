package com.example.RepairKZ_Backend.model

import com.example.RepairKZ_Backend.common.enums.StatusOfUser

data class ChangeStatusRequestDTO(
    val statusOfUser: StatusOfUser,
    val masterData: MasterRequestDTO? = null
)
