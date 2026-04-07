package com.example.RepairKZ_Backend.repository

import com.example.RepairKZ_Backend.entity.Master
import com.example.RepairKZ_Backend.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface MasterRepository : JpaRepository<Master, Long> {
    fun findByUser(user: User): Master?
    fun findByUserId(userId: Long): Master?

    @Query("SELECT m FROM Master m WHERE m.user.id != :id AND m.user.status != 'CLIENT'")
    fun findAllWithoutId(id: Long): List<Master>
}