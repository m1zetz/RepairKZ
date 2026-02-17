package com.example.repairkz.ui.features.settings

import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.intents.AppIntent
import com.example.repairkz.common.models.User
import com.example.repairkz.ui.features.UserInfo.UserTypes

sealed class SettingsState{
    object Loading : SettingsState()
    data class Success(val userData: User) : SettingsState()
    data class Error(val message: String) : SettingsState()
}


sealed class SettingIntent{
    data class toUserScreen(val id: Int) : SettingIntent()

    data class BecomeAMaster(val status: StatusOfUser) : SettingIntent()
}


sealed class SettingsEffects {
    data class NavigateToUserInfo(val id: Int) : SettingsEffects()
}