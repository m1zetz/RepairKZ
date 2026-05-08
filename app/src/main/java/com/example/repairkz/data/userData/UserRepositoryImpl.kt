package com.example.repairkz.data.userData

import android.util.Log
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User
import com.example.repairkz.data.local.dao.MasterDao
import com.example.repairkz.data.local.dao.UserDao
import com.example.repairkz.data.local.entity.MasterEntity
import com.example.repairkz.data.remote.api.UserApi
import com.example.repairkz.data.remote.dto.order.ChangeStatusRequestDTO
import com.example.repairkz.data.remote.dto.FullUserRequestDTO
import com.example.repairkz.data.remote.dto.MasterResponseDTO
import com.example.repairkz.data.remote.dto.StatusRequestDTO
import com.example.repairkz.data.remote.dto.UpdatePhotoResponseDTO
import com.example.repairkz.data.remote.dto.UserResponseDTO
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import okhttp3.MultipartBody

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val masterDao: MasterDao,
    private val userApi: UserApi,
) : UserRepository {
    private val _userData = MutableStateFlow<User?>(null)
    override val userData = _userData.asStateFlow()

    override suspend fun fetchUserData(): User? {

        val currentState = userData.value
        return currentState
    }

    override suspend fun saveUserToLocal(user: User) {

        _userData.value = user
        userDao.saveUser(user.toEntity())
        if (user is Master){
            val entity = MasterEntity(
                userId = user.id.toLong(),
                experienceInYears = user.experienceInYears,
                masterSpecialization = user.masterSpecialization,
                description = user.description
            )
            masterDao.saveMaster(entity)
        }
    }


    override suspend fun getRoomData() {

        val data = userDao.getUser()
        val userEntity = data?.user
        if (userEntity != null) {
            _userData.value = when (userEntity.status) {
                StatusOfUser.MASTER -> {
                    val masterEntity = data.master
                    userEntity.toMaster(masterEntity)
                }
                else -> userEntity.toUser()
            }
        }

    }

    override suspend fun updateUserData(user: User) : Result<Unit> {

        val request = FullUserRequestDTO(
            user.toResponseDTO(),
            experienceInYears = (user as? Master)?.experienceInYears,
            description = (user as? Master)?.description,
            masterSpecialization = (user as? Master)?.masterSpecialization
        )
        val response = userApi.updateUser(request)

        return if (response.isSuccessful) {
            val data = response.body()
            if (data!=null) {
                val userData = data.user

                val finalUser = if (user is Master) {
                    user.copy(
                        firstName = userData.firstName,
                        lastName = userData.lastName,
                        email = userData.email,
                        phoneNumber = userData.phone,
                        city = userData.city,
                        experienceInYears = data.experienceInYears ?: user.experienceInYears,
                        description = data.description ?: user.description,
                        masterSpecialization = data.masterSpecialization ?: user.masterSpecialization
                    )
                } else {
                    user.copy(
                        firstName = userData.firstName,
                        lastName = userData.lastName,
                        email = userData.email,
                        phoneNumber = userData.phone,
                        city = userData.city
                    )
                }
                _userData.value = finalUser
                userDao.saveUser(finalUser.toEntity())
                if(finalUser is Master){
                    masterDao.saveMaster(
                        MasterEntity(
                            userId = finalUser.id,
                            description =  finalUser.description,
                            experienceInYears = finalUser.experienceInYears,
                            masterSpecialization =  finalUser.masterSpecialization
                        )
                    )
                }


            }

            Result.success(Unit)
        } else {
            Result.failure(Exception("Fail to update user"))
        }
    }

    override suspend fun updateUserPhoto(id: Long, file: MultipartBody.Part): Result<UpdatePhotoResponseDTO> {
        return try {
            val response = userApi.updateUserPhoto(id, file)
            if(response.isSuccessful){
                val body = response.body()!!
                val currentUser = _userData.value
                    ?: return Result.failure(Exception("User not found"))
                val updatedUser = if (currentUser is Master) {
                    currentUser.copyMaster(userPhotoUrl = body.photoUrl)
                } else {
                    currentUser.copy(userPhotoUrl = body.photoUrl)
                }
                _userData.value = updatedUser
                _userData.value = updatedUser
                userDao.updateUserPhoto(updatedUser.toEntity())
                Result.success(body)
            }else{
                val errorMsg = response.errorBody()?.string()
                Result.failure(Exception(errorMsg))
            }
        } catch(e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun updateUserStatus(id: Long, dto: ChangeStatusRequestDTO) : Result<Unit> {
        return try {
            val currentUser =
                _userData.value ?: return Result.failure(Exception("current user is null"))
            val response = userApi.updateStatus(id, dto)
            return if (response.isSuccessful) {

                val data = response.body()

                if (data == null){
                    return Result.failure(Exception("Response body is null"))
                }
                val updatedUser = if (dto.statusOfUser == StatusOfUser.MASTER) {
                    currentUser.toMaster(
                        statusOfUser = StatusOfUser.MASTER
                    ).copy(
                        experienceInYears = data.experienceInYears ?: 0,
                        description = data.description ?: "",
                        masterSpecialization = data.masterSpecialization
                            ?: MasterSpetializationsEnum.UNKNOWN
                    )
                } else {
                    currentUser.copy(statusOfUser = StatusOfUser.CLIENT)
                }
                _userData.value = updatedUser

                userDao.saveUser(updatedUser.toEntity())


                if (updatedUser is Master) {
                    masterDao.saveMaster(
                        MasterEntity(
                            userId = updatedUser.id,
                            description = updatedUser.description,
                            experienceInYears = updatedUser.experienceInYears,
                            masterSpecialization = updatedUser.masterSpecialization
                        )
                    )
                }
                Result.success(Unit)

            } else {
                Result.failure(Exception("Network error"))
            }
        } catch (e: Exception){
            Result.failure(Exception(e.message))
        }

    }
}