package com.example.repairkz.ui.features.auth.signUp.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.Navigation.Routes.SIGN_UP_DATA
import com.example.repairkz.Navigation.Routes.SIGN_UP_EMAIL
import com.example.repairkz.R
import com.example.repairkz.common.enums.PhotoSourceEnum
import com.example.repairkz.common.handlers.photoPickerHandler
import com.example.repairkz.common.ui.PhotoSourceBottomSheet
import com.example.repairkz.common.ui.SignTextField
import com.example.repairkz.common.ui.UserPhoto
import com.example.repairkz.ui.features.CameraX.CameraIntent
import com.example.repairkz.ui.features.CameraX.CameraViewModel
import com.example.repairkz.ui.features.CameraX.PhotoPreview
import com.example.repairkz.ui.features.auth.signUp.SignUpEffect
import com.example.repairkz.ui.features.auth.signUp.SignUpIntent
import com.example.repairkz.ui.features.auth.signUp.SignUpViewModel

@Composable
fun SignUpData(signUpViewModel: SignUpViewModel, navController: NavController) {

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isCurrentScreen = currentBackStackEntry?.destination?.route == SIGN_UP_DATA
    val context = LocalContext.current
    val activity = LocalActivity.current as ComponentActivity
    val cameraViewModel: CameraViewModel = hiltViewModel(viewModelStoreOwner = activity)
    val state by signUpViewModel.state.collectAsState()

    LaunchedEffect("photo") {
        cameraViewModel.state.collect { state ->
            state.uri?.let {
                signUpViewModel.handleIntent(SignUpIntent.ConfirmPhoto(it))
                cameraViewModel.handleIntent(CameraIntent.ClearPhoto)
            }
        }
    }
    val action = photoPickerHandler(
        getPhotoFromMedia = { uri ->
            if (uri != null) {
                signUpViewModel.handleIntent(SignUpIntent.GetPhotoFromMedia(uri))

            }
        },
        navController = navController,
        context = context
    )


    LaunchedEffect("effects") {
        signUpViewModel.channel.collect { effect ->
            when (effect) {
                is SignUpEffect.OpenPhotoPicker -> {
                    when (effect.typeOfSelect) {
                        PhotoSourceEnum.CAMERA -> action.launchCamera()
                        PhotoSourceEnum.GALLERY -> action.launchGallery()
                    }
                }
                is SignUpEffect.NavigateToMainWindow -> {
                    navController.navigate(Routes.MAIN_WINDOW){
                        popUpTo(0) { inclusive = true }
                    }

                }

                else -> {}
            }
        }
    }

    BackHandler(enabled = isCurrentScreen) {
        if (state.userInfo.pendingPhotoUri != null) {
            signUpViewModel.handleIntent(SignUpIntent.CancelPhoto)
        } else {
            signUpViewModel.handleIntent(SignUpIntent.ResetRegistrationData)
            navController.navigate(SIGN_UP_EMAIL) {
                popUpTo(SIGN_UP_EMAIL) { inclusive = true }
            }
        }

    }
    SignUpLayout(
        signUpViewModel
    ) { state ->

        if (state.userInfo.pendingPhotoUri != null) {
            PhotoPreview(
                context,
                uri = state.userInfo.pendingPhotoUri,
                onDismissRequest = { signUpViewModel.handleIntent(SignUpIntent.CancelPhoto) },
            ) { uri ->
                uri?.let {
                    signUpViewModel.handleIntent(SignUpIntent.ConfirmPhoto(it))
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    8.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {
                if(state.isLoading){
                    CircularProgressIndicator()
                }
                else{
                    UserPhoto(
                        state.userInfo.photoUri?.toString(),
                        changeAvatarIntent = {
                            signUpViewModel.handleIntent(SignUpIntent.OpenSheet)
                        }
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    SignTextField(
                        state.userInfo.firstName,
                        { newValue ->
                            signUpViewModel.handleIntent(SignUpIntent.ChangeFirstName(newValue))
                        },
                        state.userInfo.firstNameError,
                        placeholder = R.string.enter_first_name
                    )
                    SignTextField(
                        state.userInfo.lastName,
                        { newValue ->
                            signUpViewModel.handleIntent(SignUpIntent.ChangeLastName(newValue))
                        },
                        state.userInfo.lastNameError,
                        placeholder = R.string.enter_last_name
                    )
                    SignTextField(
                        state.password,
                        { newValue ->
                            signUpViewModel.handleIntent(SignUpIntent.ChangePassword(newValue))
                        },
                        state.passwordError,
                        placeholder = R.string.come_up_with_password
                    )
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            signUpViewModel.handleIntent(SignUpIntent.NavigateToMainWindow)
                        }
                    ) {
                        Text(stringResource(R.string.sign_up))
                    }
                    if (state.avatarSheetState) {
                        PhotoSourceBottomSheet(
                            closeSheet = {
                                signUpViewModel.handleIntent(SignUpIntent.CloseSheet)
                            },
                            fromCamera = {
                                signUpViewModel.handleIntent(
                                    SignUpIntent.ChangeAvatar(
                                        PhotoSourceEnum.CAMERA
                                    )
                                )
                            },
                            fromGallery = {
                                signUpViewModel.handleIntent(
                                    SignUpIntent.ChangeAvatar(
                                        PhotoSourceEnum.GALLERY
                                    )
                                )
                            }
                        )
                    }
                }

            }
        }
    }

}

