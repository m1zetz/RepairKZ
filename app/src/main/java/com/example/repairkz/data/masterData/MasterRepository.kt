package com.example.repairkz.data.masterData

import com.example.repairkz.common.models.Master

interface MasterRepository {
    suspend fun fetchMasterById(id: Int) : Result<Master>
}