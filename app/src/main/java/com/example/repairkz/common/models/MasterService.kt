package com.example.repairkz.common.models

import com.example.repairkz.data.local.entity.ServiceEntity

data class MasterService(
    val id: Long,
    val masterId: Long,
    val name: String,
    val price: Int,
    val position: Int
){
    fun toEntity() : ServiceEntity{
        return ServiceEntity(
            this.id,
            this.masterId,
            this.name,
            this.price,
            this.position
        )
    }
}