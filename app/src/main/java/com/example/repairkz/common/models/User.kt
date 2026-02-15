package com.example.repairkz.common.models

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.StatusOfUser

open class User(
    open val userId: Int,
    open val userPhotoUrl: String?,
    open val firstName: String,
    open val lastName: String,
    open val email: String,
    open val phoneNumber: String,
    open val status: StatusOfUser,
    open val city: CitiesEnum
)
