package com.example.RepairKZ_Backend.model

data class MasterServiceDTO(
    val id: Long? = null,
    val masterId: Long,
    val service: String,
    val price: Int,
    val position: Int
){

}
