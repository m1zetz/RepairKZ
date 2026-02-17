package com.example.repairkz.ui.features.UserInfo

import android.os.Message
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User

sealed class UserState {
    object Loading : UserState()
    data class Success(val userTypes: UserTypes) : UserState()
    data class Error(val message: String) : UserState()
}

data class CommonInfo(
    val photoUrl: String,
    val firstName: String,
    val lastName: String,

)
sealed class UserTypes{

    abstract val commonInfo: CommonInfo
    data class IsCurrentUser(val user: User, override val commonInfo: CommonInfo) : UserTypes()
    data class IsCurrentMaster(val master: Master, override val commonInfo: CommonInfo): UserTypes()
    data class IsOtherMaster(val master: Master, override val commonInfo: CommonInfo) : UserTypes()
}

sealed class UserIntent{

    object EditProfile: UserIntent()
    sealed class MasterProfileIntent : UserIntent(){
        data class DoOrder(val userId: Int): MasterProfileIntent()
        data class AddToFavorites(val userId: Int): MasterProfileIntent()
        data class Report(val masterId: Int): MasterProfileIntent()
    }
}