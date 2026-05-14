package com.example.repairkz.data.remote.dto

import com.example.repairkz.common.models.MasterService
import com.example.repairkz.data.local.entity.ServiceEntity

data class MasterServiceDTO(
    val id: Long? = null,
    val masterId: Long,
    val service: String,
    val price: Int,
    val position: Int? = null
){
    fun toEntity() = ServiceEntity(
        id = id ?: 0,
        masterId = masterId,
        service = service,
        price = price,
        position = position!!
    )
    fun toModel() = MasterService(
        id = id!!,
        masterId = masterId,
        name = service,
        price = price,
        position = position!!
    )
}