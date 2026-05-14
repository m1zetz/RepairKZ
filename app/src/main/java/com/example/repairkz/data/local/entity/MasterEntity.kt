package com.example.repairkz.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.data.remote.dto.MasterServiceDTO

@Entity(
    "master",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class MasterEntity(
    @PrimaryKey()
    @ColumnInfo(name = "master_id")
    val masterId: Long,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    val experienceInYears: Int? = null,
    val description: String? = null,
    val masterSpecialization: MasterSpetializationsEnum? = null
)
