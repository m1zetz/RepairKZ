package com.example.repairkz.data.local.entity

import android.util.Log
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
    val masterId: Long? = null,
    val experienceInYears: Int? = null,
    val description: String? = null,
    val masterSpecialization: MasterSpetializationsEnum? = null
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

    fun toMaster(services: List<ServiceEntity> = emptyList()): Master {
        return Master(
            id = this.id ?: 0,
            masterId = this.masterId?:0,
            userPhotoUrl = this.userPhotoUrl,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            phoneNumber = this.phoneNumber,
            status = this.status,
            city = this.city,
            experienceInYears = this.experienceInYears ?:0,
            description = this.description ?: "",
            masterSpecialization = this.masterSpecialization ?: MasterSpetializationsEnum.UNKNOWN,
            services = services.map { it.toModel() }
        )
    }
}
