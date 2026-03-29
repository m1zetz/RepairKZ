package com.example.repairkz.common.models

import com.example.repairkz.R
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser

data class Master(
    override val id: Int,
    override val userPhotoUrl: String?,
    override val firstName: String,
    override val lastName: String,
    override val email: String,
    override val phoneNumber: String,
    override val status: StatusOfUser,
    override val city: CitiesEnum,
    val experienceInYears: Int = 0,
    val description: String = "",
    val masterSpecialization: MasterSpetializationsEnum = MasterSpetializationsEnum.UNKNOWN

) : User(id, userPhotoUrl, firstName, lastName, email, phoneNumber, status, city){
    override fun toUser(): User {
        return User(
            id = this.id,
            userPhotoUrl = this.userPhotoUrl,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            phoneNumber = this.phoneNumber,
            status = StatusOfUser.CLIENT,
            city = this.city
        )
    }

    fun copyMaster(userPhotoUrl: String? = this.userPhotoUrl): Master {
        return Master(
            id = this.id,
            userPhotoUrl = userPhotoUrl,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            phoneNumber = this.phoneNumber,
            status = this.status,
            city = this.city,
            experienceInYears = this.experienceInYears,
            description = this.description,
            masterSpecialization = this.masterSpecialization
        )
    }
    override val displayDescriptionRes : Int
        get() = this.masterSpecialization.resID


}
