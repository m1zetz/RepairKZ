package com.example.repairkz.domain.useCases.userData

import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.data.masterData.MasterRepository
import com.example.repairkz.data.userData.UserRepository
import com.example.repairkz.ui.features.UserInfo.CommonInfo
import com.example.repairkz.ui.features.UserInfo.UserState
import com.example.repairkz.ui.features.UserInfo.UserTypes
import java.lang.Exception
import javax.inject.Inject

class GetProfileTypeUseCase @Inject constructor(
    val userRepository: UserRepository,
    val masterRepository: MasterRepository,
) {
    suspend operator fun invoke(comingId: Int?): Result<UserTypes> {
        return userRepository.fetchUserData().fold(
            onSuccess = { user ->
                if (user.userId == comingId || comingId == null) {
                    if(user is Master){
                        Result.success(UserTypes.IsCurrentMaster(user, user.getCommonInfo()))
                    }
                    else{
                        Result.success(UserTypes.IsCurrentUser(user, user.getCommonInfo()))
                    }
                } else {
                    val masterResult = masterRepository.fetchMasterById(comingId)
                    masterResult.map { master ->
                        val masterCommonInfo = CommonInfo(master.firstName,master.lastName,master.userPhotoUrl?:"")
                        UserTypes.IsOtherMaster(master, master.getCommonInfo())
                    }
                }
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }
}