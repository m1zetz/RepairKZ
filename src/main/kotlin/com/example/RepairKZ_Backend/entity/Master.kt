package com.example.RepairKZ_Backend.entity

import com.example.RepairKZ_Backend.common.enums.MasterSpetializationsEnum
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "masters")
data class Master(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User,

    val experienceInYears: Int? = null,
    val description: String? = null,
    val masterSpecialization: MasterSpetializationsEnum? = null
)
