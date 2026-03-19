package com.example.RepairKZ_Backend.repository

import com.example.RepairKZ_Backend.entity.Master
import com.example.RepairKZ_Backend.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface MasterRepository : JpaRepository<Master, Long> {
    fun findByUser(user: User): Master?
}