package com.example.repairkz.data.remote.dto.order

import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.data.remote.dto.MasterRequestDTO

data class ChangeStatusRequestDTO(
    val statusOfUser: StatusOfUser,
    val masterData: MasterRequestDTO? = null
)