package com.example.repairkz.common.models

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.data.local.entity.UserEntity
import com.example.repairkz.data.remote.dto.UserRegistrationDTO
import com.example.repairkz.data.remote.dto.UserResponseDTO
import com.example.repairkz.ui.features.UserInfo.BusinessCardData

open  class User(
    open val id: Long,
    open val userPhotoUrl: String?,
    open val firstName: String,
    open val lastName: String,
    open val email: String,

    open val phoneNumber: String,
    open val status: StatusOfUser,
    open val city: CitiesEnum? = CitiesEnum.UNKNOWN,
    open val password: String? = null,
){
    fun copy(
        userId: Long = this.id,
        userPhotoUrl: String? = this.userPhotoUrl,
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        email: String = this.email,
        phoneNumber: String = this.phoneNumber,
        statusOfUser: StatusOfUser = this.status,
        city: CitiesEnum? = this.city
    ) : User{
        val updatedUser = User(userId,userPhotoUrl,firstName,lastName,email,phoneNumber,statusOfUser,city)
        return updatedUser
    }

    fun getCommonInfo(
        id: Long = this.id,
        userPhotoUrl: String? = this.userPhotoUrl,
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        isMe: Boolean
    ) : BusinessCardData{
        return BusinessCardData(
            id,
            userPhotoUrl?: "",
            firstName,
            lastName,
            isMe = isMe
        )
    }

    fun toMaster(
        userId: Long = this.id,
        masterId: Long,
        userPhotoUrl: String? = this.userPhotoUrl,
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        email: String = this.email,
        phoneNumber: String = this.phoneNumber,
        statusOfUser: StatusOfUser = this.status,
        city: CitiesEnum? = this.city
    ) : Master {

        val master = Master(
            userId,
            masterId,
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
    fun toMasterWithData(
        userId: Long = this.id,
        masterId: Long,
        userPhotoUrl: String? = this.userPhotoUrl,
        firstName: String = this.firstName,
        lastName: String = this.lastName,
        email: String = this.email,
        phoneNumber: String = this.phoneNumber,
        statusOfUser: StatusOfUser = this.status,
        city: CitiesEnum? = this.city,
        desc: String,
        spec: MasterSpetializationsEnum,
        exp: Int
    ) : Master {

        val master = Master(
            userId,
            masterId,
            userPhotoUrl,
            firstName,
            lastName,
            email,
            phoneNumber,
            StatusOfUser.MASTER,
            city,
            exp,
            desc,
            spec
        )
        return master
    }

    open fun toUser() : User{
        return this
    }
    fun toResponseDTO() = UserResponseDTO(
        id = this.id,
        userPhotoUrl = this.userPhotoUrl,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        city = this.city,
        phone = this.phoneNumber,
        status = this.status
    )
    open fun toEntity(): UserEntity {
        return UserEntity(
            id = this.id,
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id &&
                userPhotoUrl == other.userPhotoUrl &&
                firstName == other.firstName &&
                lastName == other.lastName &&
                email == other.email &&
                phoneNumber == other.phoneNumber &&
                status == other.status &&
                city == other.city
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}
