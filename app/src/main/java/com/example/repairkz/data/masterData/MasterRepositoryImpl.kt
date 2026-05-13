package com.example.repairkz.data.masterData

import android.util.Log
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.data.local.dao.MasterDao
import com.example.repairkz.data.local.dao.ServiceDao
import com.example.repairkz.data.local.entity.ServiceEntity
import com.example.repairkz.data.remote.api.MasterApi
import com.example.repairkz.data.remote.api.ServicesApi
import com.example.repairkz.data.remote.dto.MasterInfoDTO
import com.example.repairkz.data.remote.dto.MasterServiceDTO
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MasterRepositoryImpl @Inject constructor(
    private val masterApi: MasterApi,
    private val servicesApi: ServicesApi,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val serviceDao: ServiceDao,
    private val masterDao: MasterDao
) : MasterRepository {

    private val _masters = MutableStateFlow<List<Master>?>(null)
    override val masters = _masters.asStateFlow()

    override fun getServices() : Flow<List<ServiceEntity>> {
        return serviceDao.getServices()
    }

    override suspend fun updateService(masterServiceDTO: MasterServiceDTO): Result<MasterServiceDTO> {
        return try {
            val response = servicesApi.updateService(masterServiceDTO)
            val service = response.body()?: return Result.failure(Exception("Network error"))
            serviceDao.upsertMasterService(
                service.toEntity()
            )
            Result.success(service)
        } catch(e: Exception){
            Result.failure(Exception(e.message))
        }
    }

    override suspend fun createService(masterServiceDTO: MasterServiceDTO): Result<MasterServiceDTO> {
        return try {
            val response = servicesApi.createService(masterServiceDTO)
            val service = response.body()?: return Result.failure(Exception("Network error"))
            serviceDao.upsertMasterService(
                service.toEntity()
            )
            serviceDao.getServices().first()
            Result.success(service)
        } catch(e: Exception){
            Result.failure(Exception(e.message))
        }
    }

    override suspend fun deleteService(id: Long): Result<Unit> {
        return try {
            val response = servicesApi.deleteService(id)
            if(response.isSuccessful){
                serviceDao.deleteService(id)
                Result.success(Unit)
            } else{
                Result.failure(Exception(response.message()))
            }

        } catch(e: Exception){
            Result.failure(Exception(e.message))
        }
    }


    override suspend fun getMasters(): Result<List<Master>> {
        val userData =
            getUserDataUseCase() ?: return Result.failure(Exception("Current user is null"))
        val response = masterApi.getMasters(userData.id)
        val list = response.body()?: return Result.failure(Exception("Network error"))
        _masters.value = list.map { dto ->
            Master(
                id = dto.id,
                userPhotoUrl = dto.userPhotoUrl,
                firstName = dto.firstName,
                lastName = dto.lastName,
                masterSpecialization = dto.masterSpecialization,
                masterId = dto.id
            )
        }
        return Result.success(_masters.value!!)
    }

    override suspend fun fetchMasterById(id: Long): Result<Master> {
        return try{
            val response = masterApi.getMasterById(id)
            if(response.isSuccessful){
                val body = response.body() ?: return Result.failure(Exception("Empty body"))
                val userData = body.user
                val masterId = body.masterId ?: return Result.failure(Exception("Master id is null"))
                val master = Master(
                    id = masterId,
                    userPhotoUrl = userData.userPhotoUrl,
                    firstName = userData.firstName,
                    lastName = userData.lastName,
                    email = userData.email,
                    phoneNumber = userData.phone,
                    status = StatusOfUser.MASTER,
                    city = userData.city,
                    experienceInYears = body.experienceInYears?:0,
                    description = body.description?:"",
                    masterSpecialization = body.masterSpecialization?: MasterSpetializationsEnum.UNKNOWN,
                    services = body.services,
                    masterId = body.masterId
                )
                Result.success(master)
            } else {
                Result.failure(Exception(response.message()))
            }

        } catch (e: Exception){
            Result.failure(Exception(e.message))
        }

    }
}