package com.example.repairkz.data.masterData

import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User
import com.example.repairkz.data.local.entity.ServiceEntity
import com.example.repairkz.data.remote.dto.MasterInfoDTO
import com.example.repairkz.data.remote.dto.MasterServiceDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MasterRepository {
    val masters: StateFlow<List<Master>?>
    suspend fun getMasters() : Result<List<Master>>
    suspend fun fetchMasterById(id: Long) : Result<Master>
    fun getServices() : Flow<List<ServiceEntity>>
    suspend fun updateService(masterServiceDTO: MasterServiceDTO): Result<MasterServiceDTO>
    suspend fun createService(masterServiceDTO: MasterServiceDTO): Result<MasterServiceDTO>
    suspend fun deleteService(id:Long): Result<Unit>

}