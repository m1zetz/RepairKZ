package com.example.repairkz.data.userData

import android.util.Log
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User
import com.example.repairkz.data.local.dao.MasterDao
import com.example.repairkz.data.local.dao.UserDao
import com.example.repairkz.data.local.entity.MasterEntity
import com.example.repairkz.data.remote.api.UserApi
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val masterDao: MasterDao,
    private val userApi: UserApi
) : UserRepository {
    private val _userData = MutableStateFlow<User?>(null)
    override val userData = _userData.asStateFlow()

    override suspend fun fetchUserData(): User? {
        val currentState = userData.value
        return currentState
    }


    override suspend fun updateUserData(user: User) {
        _userData.value = user
        userDao.saveUser(user.toEntity())
        if(user is Master){
            masterDao.saveMaster(MasterEntity(
                userId = user.id.toLong(),
                description = user.description,
                experienceInYears = user.experienceInYears,
                masterSpecialization = user.masterSpecialization

            ))
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

    override suspend fun updateUserStatus(statusOfUser: StatusOfUser) {
        when (statusOfUser) {
            StatusOfUser.CLIENT -> {
                _userData.update {
                    it?.toUser()
                }
            }
            StatusOfUser.MASTER -> {
                _userData.update {
                    it?.toMaster()
                }
            }
        }
        val currentUser = _userData.value
        userDao.saveUser(currentUser!!.toEntity())
        if (currentUser is Master){
            masterDao.saveMaster(
                masterEntity = MasterEntity(
                    userId = currentUser.id.toLong(),
                    experienceInYears = currentUser.experienceInYears,
                    masterSpecialization = currentUser.masterSpecialization
                )
            )
        }
        userApi.updateStatus(currentUser.id, currentUser.status)
    }

}