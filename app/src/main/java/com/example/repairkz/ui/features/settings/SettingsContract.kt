package com.example.repairkz.ui.features.settings

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import com.example.repairkz.domain.errors.SettingsError
import com.example.repairkz.ui.base.UiEffect
import com.example.repairkz.ui.base.UiIntent
import com.example.repairkz.ui.base.UiState


data class SettingsState (
    val user: User? = null,
    val isChangeStatusLoading: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
): UiState

data class ChangeStatusConfig(
    val icon: ImageVector,
    val textRes: Int,
    val status: StatusOfUser
)

sealed class SettingsIntent : UiIntent {
    object ToUserScreen : SettingsIntent()

    data class SwitchStatus(val status: StatusOfUser) : SettingsIntent()

    object Exit : SettingsIntent()
}


sealed class SettingsEffect : UiEffect{
    object NavigateToUserInfo : SettingsEffect()
    data class ShowError(val error: SettingsError) : SettingsEffect()
    object NavigateToLogin : SettingsEffect()

}