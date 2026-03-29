package com.example.repairkz.common.models

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.data.local.entity.UserEntity
import com.example.repairkz.data.remote.dto.UserRegistrationDTO
import com.example.repairkz.data.remote.dto.UserResponseDTO
import com.example.repairkz.ui.features.UserInfo.CommonInfo

open class User(
    open val id: Int,
    open val userPhotoUrl: String?,
    open val firstName: String,
    open val lastName: String,
    open val email: String,

    open val phoneNumber: String,
    open val status: StatusOfUser,
    open val city: CitiesEnum,
    open val password: String? = null,
){
    fun copy(
        userId: Int = this.id,
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
        id: Int = this.id,
        userPhotoUrl: String? = this.userPhotoUrl,
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        isMe: Boolean
    ) : CommonInfo{
        return CommonInfo(
            id,
            userPhotoUrl?: "",
            firstName,
            lastName,
            isMe = isMe
        )
    }

    fun toMaster(
        userId: Int = this.id,
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
    fun toResponseDTO() = UserResponseDTO(
        id = this.id.toLong(),
        userPhotoUrl = this.userPhotoUrl,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        city = this.city,
        phone = this.phoneNumber,
        status = this.status
    )
    fun toEntity(): UserEntity {
        return UserEntity(
            id = this.id.toLong(),
            userPhotoUrl = this.userPhotoUrl,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            phoneNumber = this.phoneNumber,
            status = this.status,
            city = this.city
        )
    }
    fun toCreateUserDTO() = UserRegistrationDTO(
        firstName = firstName,
        lastName = lastName,
        userPhotoUrl = userPhotoUrl,
        email = email,
        password = password,
        phone = phoneNumber,
        status = status,
        city = city
    )

    open val displayDescriptionRes: Int
        get() = this.status.resID

}
