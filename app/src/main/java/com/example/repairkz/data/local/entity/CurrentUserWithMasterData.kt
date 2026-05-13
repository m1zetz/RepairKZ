package com.example.repairkz.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CurrentUserWithMasterData(
    @Embedded
    var user: UserEntity? = null,
    @Relation(parentColumn = "id", entityColumn = "user_id")
    var master: MasterEntity? = null,
    @Relation(parentColumn = "id", entityColumn = "master_id", entity = ServiceEntity::class)
    var services: List<ServiceEntity> = emptyList()
)
