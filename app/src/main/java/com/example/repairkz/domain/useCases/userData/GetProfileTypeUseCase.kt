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
        val user =  userRepository.fetchUserData()
        user?.let{
            if (user.userId == comingId || comingId == null) {
                if(user is Master){
                    return Result.success(UserTypes.IsCurrentMaster(user, user.getCommonInfo(isMe = true)))
                }
                else{
                    return Result.success(UserTypes.IsCurrentUser(user, user.getCommonInfo(isMe = true)))
                }
            } else {
                val masterResult = masterRepository.fetchMasterById(comingId)
                return masterResult.map { master ->
                    UserTypes.IsOtherMaster(master, master.getCommonInfo(isMe = false))
                }
            }
        }
        return Result.failure(kotlin.Exception("null_id"))
    }
}