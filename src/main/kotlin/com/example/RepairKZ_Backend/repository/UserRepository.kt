package com.example.RepairKZ_Backend.repository

import com.example.RepairKZ_Backend.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByName(name: String): User?
    fun findByEmail(email: String): User?
    fun findByPhoneNumber(number: String): User?
}