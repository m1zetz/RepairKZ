package com.example.repairkz.data.local.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.example.repairkz.data.local.entity.MasterEntity

@Dao
interface MasterDao{
    @Upsert
    suspend fun saveMaster(masterEntity: MasterEntity)
}