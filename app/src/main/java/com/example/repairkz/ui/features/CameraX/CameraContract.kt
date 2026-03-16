package com.example.repairkz.ui.features.CameraX

import android.net.Uri

data class CameraState(
    val uri: Uri? = null
)

sealed class CameraIntent {
    object ClearPhoto : CameraIntent()
    data class SetPhoto(val uri: Uri) : CameraIntent()
}