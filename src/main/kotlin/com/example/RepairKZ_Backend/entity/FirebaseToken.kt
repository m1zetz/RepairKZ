package com.example.RepairKZ_Backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "firebase_token")
data class FirebaseToken(
    @Id
    val userId: Long,
    @Column(nullable = false, unique = true)
    val token: String
)