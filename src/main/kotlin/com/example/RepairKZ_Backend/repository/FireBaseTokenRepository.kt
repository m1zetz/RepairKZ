package com.example.RepairKZ_Backend.repository

import com.example.RepairKZ_Backend.entity.FirebaseToken
import org.springframework.data.jpa.repository.JpaRepository

interface FirebaseTokenRepository : JpaRepository<FirebaseToken, Long> {
    fun findByUserId(userId: Long): FirebaseToken?
    fun deleteByToken(token: String)
}