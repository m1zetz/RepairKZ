package com.example.repairkz.Navigation

import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
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
    parentRoute: String,
    route: String,
    getViewModel: @Composable (NavBackStackEntry) -> CameraCapable
) {
    navigation(
        startDestination = Routes.CAMERA,
        route = route
    ) {
        composable(Routes.CAMERA) { nbse ->
            val parentEntry = remember(nbse) { navController.getBackStackEntry(parentRoute) }
            val vm = getViewModel(parentEntry)
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
            val parentEntry = remember(nbse) { navController.getBackStackEntry(parentRoute) }
            val vm = getViewModel(parentEntry)
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
            Text("dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd")
        }
    }
}