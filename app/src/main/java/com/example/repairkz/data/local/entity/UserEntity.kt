package com.example.repairkz.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User

@Entity("user")
data class UserEntity(
    @PrimaryKey()
    val id: Long? = 0,
    val userPhotoUrl: String?,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val status: StatusOfUser,
    val city: CitiesEnum?,
){
    fun toUser(): User {
        return User(
            id = this.id ?: 0,
            userPhotoUrl = this.userPhotoUrl,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            phoneNumber = this.phoneNumber,
            status = this.status,
            city = this.city
        )
    }

    fun toMaster(masterEntity: MasterEntity?): Master {
        return Master(
            id = this.id ?: 0,
            masterId = masterEntity?.masterId?:0,
            userPhotoUrl = this.userPhotoUrl,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            phoneNumber = this.phoneNumber,
            status = this.status,
            city = this.city,
            experienceInYears = masterEntity?.experienceInYears?:0,
            description = masterEntity?.description?: "",
            masterSpecialization = masterEntity?.masterSpecialization?: MasterSpetializationsEnum.UNKNOWN,
        )
    }
}
