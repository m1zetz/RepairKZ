package com.example.RepairKZ_Backend.entity

import com.example.RepairKZ_Backend.common.enums.CitiesEnum
import com.example.RepairKZ_Backend.common.enums.MasterSpetializationsEnum
import com.example.RepairKZ_Backend.model.MasterInfoDTO
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "masters")
data class Master(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,

    @Column(name = "experience_in_years")
    var experienceInYears: Int? = null,
    var description: String? = null,

    @Enumerated(EnumType.STRING)
    var masterSpecialization: MasterSpetializationsEnum? = null
){
    fun toInfoDto() : MasterInfoDTO {
        val user = this.user ?: throw Exception("User is null")
        return MasterInfoDTO(
            id = this.id!!,
            userPhotoUrl = user.userPhotoUrl,
            firstName = user.firstName,
            lastName = user.lastName,
            masterSpecialization = masterSpecialization?:MasterSpetializationsEnum.UNKNOWN
        )
    }
}
