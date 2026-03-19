package com.example.RepairKZ_Backend.entity

import com.example.RepairKZ_Backend.common.enums.CitiesEnum
import com.example.RepairKZ_Backend.common.enums.StatusOfUser
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

    val userPhotoUrl: String? = null,

    @Column(nullable = false)
    var firstName: String = "",

    @Column(nullable = false)
    val lastName: String = "",

    @Column(nullable = false)
    val phoneNumber: String = "",

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: StatusOfUser = StatusOfUser.CLIENT,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val city: CitiesEnum = CitiesEnum.UNKNOWN,

    @Column(nullable = false, unique = true)
    val email: String = "",

    @Column(nullable = false)
    val password: String = ""


)