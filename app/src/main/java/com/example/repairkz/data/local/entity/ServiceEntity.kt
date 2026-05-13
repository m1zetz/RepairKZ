package com.example.repairkz.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.data.remote.dto.MasterServiceDTO

@Entity(
    "service",
    foreignKeys = [ForeignKey(
        entity = MasterEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("master_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ServiceEntity(
    @PrimaryKey
    val id: Long? = null,
    @ColumnInfo(name = "master_id")
    val masterId: Long,
    val service: String,
    val price: Int,
    val position: Int? = null,
){
    fun toDto() = MasterServiceDTO(
        id = id,
        masterId = masterId,
        service = service,
        price = price,
        position = position
    )
}