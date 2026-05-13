package com.example.repairkz.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.repairkz.data.local.entity.MasterEntity
import com.example.repairkz.data.local.entity.ServiceEntity
import com.example.repairkz.data.remote.dto.MasterServiceDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Upsert
    suspend fun upsertMasterService(serviceEntity: ServiceEntity)
    @Query("SELECT * FROM service ORDER BY position")
    fun getServices(): Flow<List<ServiceEntity>>
    @Query("DELETE FROM service WHERE id = :id")
    suspend fun deleteService(id: Long)
}