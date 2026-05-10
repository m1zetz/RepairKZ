package com.example.repairkz.data.masterData

import android.util.Log
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.data.remote.api.MasterApi
import com.example.repairkz.data.remote.dto.MasterInfoDTO
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MasterRepositoryImpl @Inject constructor(
    private val masterApi: MasterApi,
    private val getUserDataUseCase: GetUserDataUseCase,
) : MasterRepository {

    private val _masters = MutableStateFlow<List<Master>?>(null)
    override val masters = _masters.asStateFlow()

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
                masterSpecialization = dto.masterSpecialization
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
                    services = body.services
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