package com.example.repairkz.common.handlers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.common.enums.PhotoSourceEnum


private val CAMERAX_PERMISSIONS = arrayOf(
    Manifest.permission.CAMERA,
)

private fun hasRequiredPermissions(context: Context): Boolean {
    return CAMERAX_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

class PhotoActions(
    val launchCamera: () -> Unit,
    val launchGallery: () -> Unit
)
@Composable
fun photoPickerHandler(
    getPhotoFromMedia: (uri: Uri?) -> Unit,
    navController: NavController,
    context: Context
) : PhotoActions{

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {

    }
    val mediaLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri!= null){
                getPhotoFromMedia(uri)
            }

        }

    return PhotoActions(
        launchCamera = {
            if (hasRequiredPermissions(context)){
                val currentRoute = navController.currentBackStackEntry?.destination?.route
                if (currentRoute != Routes.CAMERA) {
                    navController.navigate(Routes.CAMERA)
                }
            }else{
                permissionLauncher.launch(
                    CAMERAX_PERMISSIONS[0]
                )
            }
        },
        launchGallery = {
            mediaLauncher.launch(
                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    )
}