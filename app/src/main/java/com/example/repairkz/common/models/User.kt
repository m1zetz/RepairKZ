package com.example.repairkz.common.models

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.ui.features.UserInfo.CommonInfo

open class User(
    open val userId: Int,
    open val userPhotoUrl: String?,
    open val firstName: String,
    open val lastName: String,
    open val email: String,
    open val phoneNumber: String,
    open val status: StatusOfUser,
    open val city: CitiesEnum
){
    fun copy(
        userId: Int = this.userId,
        userPhotoUrl: String? = this.userPhotoUrl,
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        email: String = this.email,
        phoneNumber: String = this.phoneNumber,
        statusOfUser: StatusOfUser = this.status,
        city: CitiesEnum = this.city
    ) : User{
        val updatedUser = User(userId,userPhotoUrl,firstName,lastName,email,phoneNumber,statusOfUser,city)
        return updatedUser
    }

    fun getCommonInfo(
        userPhotoUrl: String? = this.userPhotoUrl,
        firstName: String = this.firstName,
        lastName: String = this.lastName,
    ) : CommonInfo{
        return CommonInfo(
            userPhotoUrl?: "",
            firstName,
            lastName
        )
    }

    fun toMaster(
        userId: Int = this.userId,
        userPhotoUrl: String? = this.userPhotoUrl,
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        email: String = this.email,
        phoneNumber: String = this.phoneNumber,
        statusOfUser: StatusOfUser = this.status,
        city: CitiesEnum = this.city
    ) : Master {

        val master = Master(
            userId,
            userPhotoUrl,
            firstName,
            lastName,
            email,
            phoneNumber,
            StatusOfUser.MASTER,
            city
        )
        return master
    }

    open fun toUser() : User{
        return this
    }

    open val displayDescriptionRes: Int
        get() = this.status.resID

}
