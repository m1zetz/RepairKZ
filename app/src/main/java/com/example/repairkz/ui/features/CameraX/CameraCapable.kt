package com.example.repairkz.ui.features.CameraX

import android.net.Uri

interface CameraCapable {
    fun onPhotoSelected(uri: Uri)
    fun getPreviewUri(): Uri?
}