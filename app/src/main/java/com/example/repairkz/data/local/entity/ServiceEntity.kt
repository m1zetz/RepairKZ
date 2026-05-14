package com.example.repairkz.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.data.remote.dto.MasterServiceDTO

@Entity("service")
data class ServiceEntity(
    @PrimaryKey
    val id: Long = 0,
    @ColumnInfo(name = "master_id")
    val masterId: Long,
    val service: String,
    val price: Int,
    val position: Int? = null,
) {
    fun toDto() = MasterServiceDTO(
        id = if (id == 0L) null else id,
        masterId = masterId,
        service = service,
        price = price,
        position = position
    )
}