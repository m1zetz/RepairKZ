package com.example.RepairKZ_Backend.repository

import com.example.RepairKZ_Backend.entity.MasterServiceEntity
import com.example.RepairKZ_Backend.model.MasterServiceDTO
import org.springframework.data.jpa.repository.JpaRepository

interface MasterServiceRepository : JpaRepository<MasterServiceEntity, Long> {
    fun findByMasterId(masterId: Long): List<MasterServiceEntity>
}