package com.example.repairkz.common.models

import com.example.repairkz.common.enums.StatusOfUser

data class User(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val status: StatusOfUser
)
