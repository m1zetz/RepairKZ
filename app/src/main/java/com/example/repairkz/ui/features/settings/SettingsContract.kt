package com.example.repairkz.ui.features.settings

import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User

sealed class SettingsState{
    object Loading : SettingsState()
    data class Success(val userData: User, val currentStatus: StatusOfUser, val isMe: Boolean) : SettingsState()
    data class Error(val message: String) : SettingsState()
}


sealed class SettingIntent{
    data class toUserScreen(val id: Int) : SettingIntent()

    data class SwitchStatus(val status: StatusOfUser) : SettingIntent()
}


sealed class SettingsEffects {
    data class NavigateToUserInfo(val id: Int) : SettingsEffects()
}