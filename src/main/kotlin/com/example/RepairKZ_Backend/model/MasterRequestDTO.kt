package com.example.RepairKZ_Backend.model

import com.example.RepairKZ_Backend.common.enums.MasterSpetializationsEnum
import com.example.RepairKZ_Backend.entity.Master
import com.example.RepairKZ_Backend.entity.User

data class MasterRequestDTO(
    val userId: Long,
    val experienceInYears: Int? = null,
    val description: String? = null,
    val masterSpecialization: MasterSpetializationsEnum? = null
){
    fun toMaster(user: User): Master {
        return Master(
            null,
            user,
            this.experienceInYears,
            this.description,
            this.masterSpecialization
        )
    }
}