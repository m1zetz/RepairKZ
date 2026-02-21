package com.example.repairkz.ui.features.UserInfo

import android.content.Context
import android.content.pm.PackageManager
import com.example.repairkz.R
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.common.enums.PhotoSourceEnum
import com.example.repairkz.common.ui.StandartString
import com.example.repairkz.ui.features.CameraX.Camera
import com.example.repairkz.ui.features.profile.common.Cap
import java.io.File

private val CAMERAX_PERMISSIONS = arrayOf(
    android.Manifest.permission.CAMERA,
)

private fun hasRequiredPermissions(context: Context): Boolean {
    return CAMERAX_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfo(userInfoViewModel: UserInfoViewModel, navController: NavController) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val uiState = userInfoViewModel.uiState.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {

    }



    val mediaLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            userInfoViewModel.handleIntent(UserIntent.SelectedPhoto(uri, null))
        }


    LaunchedEffect(userInfoViewModel) {
        userInfoViewModel.channel.collect { effect ->
            when (effect) {
                is UserEffects.OpenPhotoPicker -> {
                    when (effect.typeOfSelect) {
                        PhotoSourceEnum.CAMERA -> {
                            if (hasRequiredPermissions(context)){
                                navController.navigate(Routes.CAMERA)
                            }else{
                                permissionLauncher.launch(
                                    CAMERAX_PERMISSIONS[0]
                                )
                            }
                        }

                        PhotoSourceEnum.GALLERY -> {
                            mediaLauncher.launch(
                                PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    }

                }

            }
        }
    }




    when (val state = uiState.value) {
        is UserState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is UserState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(state.message)
            }
        }

        is UserState.Success -> {
            val user = state.userTypes
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Cap(
                        commonInfo = user.commonInfo,
                        changeAvatarIntent = { intent -> userInfoViewModel.handleIntent(intent) })
                    when (user) {
                        is UserTypes.IsCurrentMaster -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("Я мастер")
                            }
                        }

                        is UserTypes.IsCurrentUser -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("Я клиент")
                            }
                        }

                        is UserTypes.IsOtherMaster -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(user.master.firstName)
                            }
                        }
                    }

                }
            }
            if (state.avatarSheetState) {
                ModalBottomSheet(
                    onDismissRequest = {
                        userInfoViewModel.handleIntent(UserIntent.CloseSheet)
                    },
                    sheetState = sheetState
                ) {
                    StandartString(
                        R.string.from_camera,
                        intent = {
                            userInfoViewModel.handleIntent(
                                UserIntent.ChangeAvatar(
                                    PhotoSourceEnum.CAMERA
                                )
                            )
                        },
                        icon = Icons.Default.Camera
                    )
                    StandartString(
                        R.string.from_camera,
                        intent = {
                            userInfoViewModel.handleIntent(
                                UserIntent.ChangeAvatar(
                                    PhotoSourceEnum.GALLERY
                                )
                            )
                        },
                        icon = Icons.Default.PermMedia
                    )
                }
            }
        }
    }
}