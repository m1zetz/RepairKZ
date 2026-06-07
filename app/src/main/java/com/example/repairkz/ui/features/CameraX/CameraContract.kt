package com.example.repairkz.ui.features.CameraX

import android.net.Uri
import com.example.repairkz.ui.base.UiEffect
import com.example.repairkz.ui.base.UiIntent
import com.example.repairkz.ui.base.UiState

data class CameraState(
    val uri: Uri? = null
)  : UiState

object CameraEffect : UiEffect
sealed class CameraIntent  : UiIntent{
    object ClearPhoto : CameraIntent()
    data class SetPhoto(val uri: Uri) : CameraIntent()
}