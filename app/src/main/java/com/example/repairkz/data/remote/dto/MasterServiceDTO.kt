package com.example.repairkz.data.remote.dto

data class MasterServiceDTO(
    val id: Long? = null,
    val masterId: Long,
    val service: String,
    val price: Int,
    val position: Int? = null
)