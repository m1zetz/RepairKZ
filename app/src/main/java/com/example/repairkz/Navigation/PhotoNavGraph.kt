package com.example.repairkz.Navigation

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.repairkz.Navigation.Routes.PROFILE_GROUP
import com.example.repairkz.ui.features.CameraX.Camera
import com.example.repairkz.ui.features.CameraX.CameraCapable
import com.example.repairkz.ui.features.CameraX.PhotoPreview
import com.example.repairkz.ui.features.UserInfo.UserInfoViewModel
import com.example.repairkz.ui.features.UserInfo.UserIntent
import com.example.repairkz.ui.features.UserInfo.UserState

fun NavGraphBuilder.photoGraph(
    navController: NavController,
    getViewModel: @Composable () -> CameraCapable
) {
    composable(Routes.CAMERA) { nbse ->
        val vm = getViewModel()
        val context = LocalContext.current
        Camera(
            context = context,
            takeNewPhoto = { uri ->
                uri?.let {
                    vm.onPhotoSelected(it)
                    navController.popBackStack()
                }
            }
        )
    }
    composable(Routes.PHOTO_PREVIEW) { nbse ->
        val context = LocalContext.current
        val vm = getViewModel()
        val uri = vm.getPreviewUri()
        uri?.let { nonNullUri ->
            PhotoPreview(
                context,
                uri = nonNullUri,
                onDismissRequest = { navController.popBackStack() },
            ) { uri ->
                uri?.let {
                    vm.onPhotoSelected(it)
                    navController.popBackStack()
                }
            }
        }

    }

}