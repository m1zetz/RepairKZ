package com.example.RepairKZ_Backend.repository

import com.example.RepairKZ_Backend.entity.EmailVerification
import org.springframework.data.jpa.repository.JpaRepository

interface EmailVerificationRepository : JpaRepository<EmailVerification, Long> {

    fun existsByEmailAndCode(email: String, code: Int): Boolean

    fun deleteByEmail(email: String) : Unit

}