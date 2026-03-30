package com.example.repairkz.ui.features.UserInfo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import com.example.repairkz.R
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.PermMedia
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.PhotoSourceEnum
import com.example.repairkz.common.handlers.photoPickerHandler
import com.example.repairkz.common.ui.EnumDropDown
import com.example.repairkz.common.ui.PhotoSourceBottomSheet
import com.example.repairkz.common.ui.ShortInputCard
import com.example.repairkz.common.ui.ShortWithComposableCard
import com.example.repairkz.common.ui.StandartString
import com.example.repairkz.common.ui.UniversalTextField
import com.example.repairkz.ui.features.CameraX.CameraIntent
import com.example.repairkz.ui.features.CameraX.CameraViewModel
import com.example.repairkz.ui.features.CameraX.PhotoPreview
import com.example.repairkz.ui.features.auth.signUp.SignUpIntent
import com.example.repairkz.ui.features.profile.common.Cap
import com.example.repairkz.ui.features.profile.master.MasterBar
import com.example.repairkz.ui.features.search.FilterIntent
import com.example.repairkz.ui.features.search.SearchIntents


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfo(userInfoViewModel: UserInfoViewModel, navController: NavController) {
    val context = LocalContext.current
    val uiState = userInfoViewModel.uiState.collectAsState()
    val activity = LocalActivity.current as ComponentActivity
    val cameraViewModel: CameraViewModel = hiltViewModel(viewModelStoreOwner = activity)
    val action = photoPickerHandler(
        getPhotoFromMedia = { uri ->
            if (uri != null)
                userInfoViewModel.handleIntent(UserIntent.GetPhotoFromMedia(uri))
        },
        navController = navController,
        context = context
    )

    LaunchedEffect("photo") {
        cameraViewModel.state.collect { state ->
            state.uri?.let {
                userInfoViewModel.handleIntent(UserIntent.ConfirmPhoto(it))
                cameraViewModel.handleIntent(CameraIntent.ClearPhoto)
            }
        }
    }

    LaunchedEffect(userInfoViewModel) {
        userInfoViewModel.channel.collect { effect ->
            when (effect) {
                is UserEffects.OpenPhotoPicker -> {
                    when (effect.typeOfSelect) {
                        PhotoSourceEnum.CAMERA -> {
                            action.launchCamera()
                        }

                        PhotoSourceEnum.GALLERY -> {
                            action.launchGallery()
                        }
                    }

                }

                UserEffects.MapsToPreview -> {
                    navController.navigate(Routes.PHOTO_PREVIEW)
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
                    if (state.pendingUri != null) {
                        PhotoPreview(
                            context,
                            uri = state.pendingUri,
                            onDismissRequest = { userInfoViewModel.handleIntent(UserIntent.CancelPhoto) },
                        ) { uri ->
                            uri?.let {
                                userInfoViewModel.handleIntent(UserIntent.ConfirmPhoto(it))
                            }
                        }
                    } else {
                        Cap(
                            commonInfo = user.commonInfo,
                            changeAvatarIntent = { userInfoViewModel.handleIntent(UserIntent.OpenSheet) })
                        when (user) {
                            is UserTypes.IsCurrentUser, is UserTypes.IsCurrentMaster -> {
                                Column(
                                    modifier = Modifier.fillMaxSize()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Top
                                ) {

                                    //общий


                                    if (user is UserTypes.IsCurrentMaster) {

                                        ShortInputCard(
                                            R.string.desc,
                                            R.string.enter_words,
                                            state.descriptionDraft,
                                            { newValue ->
                                                userInfoViewModel.handleIntent(
                                                    UserIntent.CurrentMasterIntent.ChangeDescription(
                                                        newValue
                                                    )
                                                )
                                            },
                                            backIcon = if (state.descriptionDraft != user.master.description) Icons.Default.Check else null,
                                            action = {
                                                userInfoViewModel.handleIntent(UserIntent.CurrentMasterIntent.SaveDescription)
                                            }
                                        )

                                        ShortWithComposableCard(
                                            R.string.spec,
                                            {
                                                EnumDropDown(
                                                    R.string.choice_city,
                                                    state.specDraft,
                                                    MasterSpetializationsEnum.entries,
                                                    onSelect = { spec ->
                                                        userInfoViewModel.handleIntent(
                                                            UserIntent.CurrentMasterIntent.ChangeSpecialization(
                                                                spec
                                                            )
                                                        )

                                                    },
                                                    backIcon = if (state.specDraft != user.master.masterSpecialization) Icons.Default.Check else null,
                                                    action = {
                                                        userInfoViewModel.handleIntent(UserIntent.CurrentMasterIntent.SaveSpecialization)
                                                    }
                                                )
                                            }
                                        )

                                        ShortInputCard(
                                            R.string.exp,
                                            R.string.enter_words,
                                            state.experienceDraft.toString(),
                                            { newValue ->
                                                userInfoViewModel.handleIntent(
                                                    UserIntent.CurrentMasterIntent.ChangeExperience(
                                                        newValue
                                                    )
                                                )
                                            },
                                            backIcon = if (state.experienceDraft != user.master.experienceInYears.toString()) Icons.Default.Check else null,
                                            action = {
                                                userInfoViewModel.handleIntent(UserIntent.CurrentMasterIntent.SaveExperience)
                                            }
                                        )

                                    }
                                }

                            }

                            is UserTypes.IsOtherMaster -> {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    MasterBar(
                                        { intent ->
                                            userInfoViewModel.handleIntent(intent)
                                        },
                                        masterId = user.commonInfo.id,
                                    )
                                }
                            }

                        }
                    }


                }
            }
            if (state.avatarSheetState) {
                PhotoSourceBottomSheet(
                    closeSheet = {
                        userInfoViewModel.handleIntent(UserIntent.CloseSheet)
                    },
                    fromCamera = {
                        userInfoViewModel.handleIntent(
                            UserIntent.ChangeAvatar(
                                PhotoSourceEnum.CAMERA
                            )
                        )
                    },
                    fromGallery = {
                        userInfoViewModel.handleIntent(
                            UserIntent.ChangeAvatar(
                                PhotoSourceEnum.GALLERY
                            )
                        )
                    }
                )
            }
        }
    }
}