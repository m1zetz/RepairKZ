package com.example.repairkz.data.masterData

import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User
import kotlinx.coroutines.flow.StateFlow

interface MasterRepository {
    val masters: StateFlow<List<Master>?>
    suspend fun getMasters() : Result<List<Master>>
    suspend fun fetchMasterById(id: Int) : Result<Master>
}