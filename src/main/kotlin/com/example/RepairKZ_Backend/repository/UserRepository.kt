package com.example.RepairKZ_Backend.repository

import com.example.RepairKZ_Backend.entity.User
import com.example.RepairKZ_Backend.model.UserRegistrationDTO
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun findByIdOrNull(id: Long): User?
    fun findByLoginAndPassword(login: String, password: String): User?
}