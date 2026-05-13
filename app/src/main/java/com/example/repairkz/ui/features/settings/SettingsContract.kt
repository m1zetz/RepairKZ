package com.example.repairkz.ui.features.settings
import com.example.repairkz.R

import android.content.Context
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import com.example.repairkz.domain.errors.SettingsError


data class SettingsState(
    val user: User? = null,
    val isChangeStatusLoading: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ChangeStatusConfig(
    val icon: ImageVector,
    val textRes: Int,
    val status: StatusOfUser
)

sealed class SettingIntent {
    object ToUserScreen : SettingIntent()

    data class SwitchStatus(val status: StatusOfUser) : SettingIntent()

    object Exit : SettingIntent()
}


sealed class SettingsEffect {
    object NavigateToUserInfo : SettingsEffect()
    data class ShowError(val error: SettingsError) : SettingsEffect()
    object NavigateToLogin : SettingsEffect()

}