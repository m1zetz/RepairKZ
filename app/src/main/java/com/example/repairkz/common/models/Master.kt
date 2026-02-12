package com.example.repairkz.common.models

data class Master(
    val id: Int,
    val masterSpecialization: String,
    val masterName: String,
    val avatarURL: String? = null,
    val experienceInYears: Double,
    val servicesAndPrices: String
)
