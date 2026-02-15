package com.example.repairkz.ui.features.UserInfo

import android.os.Message
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User

sealed class UserState {
    object Loading : UserState()
    data class Success(
        val user: User,
        val clientId: Int? = null
    ) : UserState()
    data class Error(val message: String) : UserState()
}

sealed class UserIntent{

    object EditProfile: UserIntent()
    sealed class MasterProfileIntent : UserIntent(){
        data class DoOrder(val userId: Int): MasterProfileIntent()
        data class AddToFavorites(val userId: Int): MasterProfileIntent()
        data class Report(val masterId: Int): MasterProfileIntent()
    }
}