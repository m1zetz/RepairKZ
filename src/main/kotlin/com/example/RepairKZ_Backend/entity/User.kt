package com.example.RepairKZ_Backend.entity

import com.example.RepairKZ_Backend.common.enums.CitiesEnum
import com.example.RepairKZ_Backend.common.enums.StatusOfUser
import com.example.RepairKZ_Backend.model.UserResponseDTO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var userPhotoUrl: String? = null,

    @Column(nullable = false)
    var firstName: String = "",

    @Column(nullable = false)
    var lastName: String = "",

    @Column(nullable = false)
    var phoneNumber: String = "",

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: StatusOfUser = StatusOfUser.CLIENT,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var city: CitiesEnum = CitiesEnum.UNKNOWN,

    @Column(nullable = false, unique = true)
    var email: String = "",

    @Column(nullable = false)
    val password: String = ""

)