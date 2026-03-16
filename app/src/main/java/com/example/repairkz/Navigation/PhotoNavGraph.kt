package com.example.repairkz.Navigation

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.repairkz.ui.features.CameraX.Camera
import com.example.repairkz.ui.features.CameraX.CameraIntent
import com.example.repairkz.ui.features.CameraX.CameraViewModel
import com.example.repairkz.ui.features.CameraX.PhotoPreview

fun NavGraphBuilder.photoGraph(
    navController: NavController,
) {
    composable(Routes.CAMERA) {
        val activity = LocalActivity.current as ComponentActivity
        val vm: CameraViewModel = hiltViewModel(viewModelStoreOwner = activity)
        val context = LocalContext.current
        Camera(
            context = context,
            takeNewPhoto = { uri ->
                uri?.let {
                    vm.handleIntent(CameraIntent.SetPhoto(it))
                    navController.popBackStack()
                }
            }
        )
    }
}