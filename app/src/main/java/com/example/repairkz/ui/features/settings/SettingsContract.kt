package com.example.repairkz.ui.features.settings

import com.example.repairkz.common.models.User

sealed class SettingsState{
    object Loading : SettingsState()
    data class Success(val userData: User) : SettingsState()
    data class Error(val message: String) : SettingsState()
}


sealed class SettingIntent {
    data class toUserScreen(val user: User) : SettingIntent()
}


sealed class SettingsEffects {
    data class NavigateToUserInfo(val userId: Int?) : SettingsEffects()
}