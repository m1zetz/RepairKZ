package com.example.RepairKZ_Backend.repository

import com.example.RepairKZ_Backend.common.enums.StatusOfUser
import com.example.RepairKZ_Backend.entity.User
import com.example.RepairKZ_Backend.model.UserRegistrationDTO
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
    fun updateStatus(@Param("id") id: Long, @Param("status")statusOfUser: StatusOfUser)
}