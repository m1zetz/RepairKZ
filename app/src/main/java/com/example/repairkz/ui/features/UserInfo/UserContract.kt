package com.example.repairkz.ui.features.UserInfo

import android.os.Message
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User

sealed class UserState {
    object Loading : UserState()
    data class Success(
        val userData: User?,
        val masterData: Master?
    ) : UserState()
    data class Error(val message: String) : UserState()
}