package com.example.repairkz.common.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.repairkz.R
import com.example.repairkz.common.enums.PhotoSourceEnum
import com.example.repairkz.ui.features.UserInfo.UserIntent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoSourceBottomSheet(
    closeSheet:() -> Unit,
    fromCamera:() -> Unit,
    fromGallery:() -> Unit,

){
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = {
            closeSheet()
        },
        sheetState = sheetState
    ) {
        StandartString(
            R.string.from_camera,
            intent = {
                fromCamera()
            },
            icon = Icons.Default.Camera
        )
        StandartString(
            R.string.from_gallery,
            intent = {
                fromGallery()
            },
            icon = Icons.Default.PermMedia
        )
    }
}