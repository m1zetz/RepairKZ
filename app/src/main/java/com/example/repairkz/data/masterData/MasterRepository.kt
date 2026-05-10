package com.example.repairkz.data.masterData

import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User
import com.example.repairkz.data.remote.dto.MasterInfoDTO
import kotlinx.coroutines.flow.StateFlow

interface MasterRepository {
    val masters: StateFlow<List<Master>?>
    suspend fun getMasters() : Result<List<Master>>
    suspend fun fetchMasterById(id: Long) : Result<Master>

}