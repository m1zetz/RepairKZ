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
    val experienceInYears: Int? = null,
    val description: String? = null,
    val masterSpecialization: MasterSpetializationsEnum? = null

) : User(userId, userPhotoUrl, firstName, lastName, email, phoneNumber, status, city){
    override fun toUser(): User {
        return User(
            userId = this.userId,
            userPhotoUrl = this.userPhotoUrl,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            phoneNumber = this.phoneNumber,
            status = StatusOfUser.CLIENT,
            city = this.city
        )
    }
}
