package com.example.repairkz.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.repairkz.data.local.entity.MasterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MasterDao{
    @Upsert
    suspend fun saveMaster(masterEntity: MasterEntity) : Long
    @Query("SELECT * FROM master")
    fun getMaster(): Flow<MasterEntity?>
}