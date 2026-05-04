package com.example.repairkz.ui.features.settings
import com.example.repairkz.R

import android.content.Context
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import com.example.repairkz.domain.errors.SettingsError


sealed class SettingsState {
    object Loading : SettingsState()
    data class Success(
        val userData: User,
        val currentStatus: StatusOfUser,
        val isMe: Boolean,
        val isChangeStatusLoading: Boolean = false,
    ) : SettingsState()

    data class Error(val error: SettingsError) : SettingsState()
}

data class ChangeStatusConfig(
    val icon: ImageVector,
    val textRes: Int,
    val status: StatusOfUser
)

sealed class SettingIntent {
    data class ToUserScreen(val id: Long) : SettingIntent()

    data class SwitchStatus(val status: StatusOfUser) : SettingIntent()

    object Exit : SettingIntent()
}


sealed class SettingsEffect {
    data class NavigateToUserInfo(val id: Long) : SettingsEffect()
    data class ShowError(val error: SettingsError) : SettingsEffect()
    object NavigateToLogin : SettingsEffect()

}