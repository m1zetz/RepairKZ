package com.example.repairkz.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.models.MasterService
import com.example.repairkz.data.remote.dto.MasterServiceDTO

@Entity("service")
data class ServiceEntity(
    @PrimaryKey
    val id: Long = 0,
    @ColumnInfo(name = "master_id")
    val masterId: Long,
    val service: String,
    val price: Int,
    val position: Int,
) {
    fun toModel() = MasterService(
        id = id,
        masterId = masterId,
        name = service,
        price = price,
        position = position
    )
}