package com.example.repairkz.common.models

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser

data class Master(
    override val userId: Int,
    override val userPhotoUrl: String?,
    override val firstName: String,
    override val lastName: String,
    override val email: String,
    override val phoneNumber: String,
    override val status: StatusOfUser,
    override val city: CitiesEnum,
    val experienceInYears: Int,
    val description: String,
    val masterSpecialization: MasterSpetializationsEnum

) : User(userId, userPhotoUrl, firstName, lastName, email, phoneNumber, status, city)
