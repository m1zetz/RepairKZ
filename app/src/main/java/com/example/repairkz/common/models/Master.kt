package com.example.repairkz.common.models

import com.example.repairkz.R
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.data.local.entity.UserEntity
import com.example.repairkz.data.remote.dto.MasterServiceDTO

data class Master(
    override val id: Long,
    val masterId: Long,
    override val userPhotoUrl: String?,
    override val firstName: String,
    override val lastName: String,
    override val email: String = "",
    override val phoneNumber: String = "",
    override val status: StatusOfUser = StatusOfUser.MASTER,
    override val city: CitiesEnum? = null,
    val experienceInYears: Int = 0,
    val description: String = "",
    val masterSpecialization: MasterSpetializationsEnum = MasterSpetializationsEnum.UNKNOWN,
    val services: List<MasterServiceDTO> = emptyList()

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

    override fun toEntity(): UserEntity {
        return UserEntity(
            id = this.id,
            userPhotoUrl = this.userPhotoUrl,
            firstName = this.firstName,
            lastName = this.lastName,
            email = this.email,
            phoneNumber = this.phoneNumber,
            status = this.status,
            city = this.city,
            masterId = this.masterId,
            experienceInYears = this.experienceInYears,
            description = this.description,
            masterSpecialization = this.masterSpecialization
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
            masterSpecialization = this.masterSpecialization,
            masterId = this.masterId
        )
    }
    override val displayDescriptionRes : Int
        get() = this.masterSpecialization.resID


}
