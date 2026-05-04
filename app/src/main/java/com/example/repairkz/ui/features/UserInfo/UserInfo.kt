package com.example.repairkz.ui.features.UserInfo

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import com.example.repairkz.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.PhotoSourceEnum
import com.example.repairkz.common.handlers.photoPickerHandler
import com.example.repairkz.common.ui.EnumDropDown
import com.example.repairkz.common.ui.PhotoSourceBottomSheet
import com.example.repairkz.common.ui.ShortInput
import com.example.repairkz.common.ui.ShortWithComposableWOpadding
import com.example.repairkz.ui.features.CameraX.CameraIntent
import com.example.repairkz.ui.features.CameraX.CameraViewModel
import com.example.repairkz.ui.features.CameraX.PhotoPreview
import com.example.repairkz.ui.features.profile.common.Cap
import com.example.repairkz.ui.features.profile.master.MasterBar


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

                is UserEffects.NavigateToOrderRegistration -> {
                    navController.navigate("${Routes.ORDER_REG}?id=${effect.masterId}")
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
            val scrollState = rememberScrollState()
            val user = state.userTypes
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
                                    Modifier.padding(horizontal = 8.dp)
                                ) {
                                    Text(
                                        stringResource(R.string.user_data),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            letterSpacing = 1.5.sp
                                        ),
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    HorizontalDivider()
                                }
                                //общий
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        ShortInput(
                                            titleResID = R.string.enter_number,
                                            value = state.number,
                                            changeValue = { newValue ->
                                                userInfoViewModel.handleIntent(
                                                    UserIntent.ChangeNumber(
                                                        newValue
                                                    )
                                                )
                                            },
                                            keyboardType = KeyboardType.Phone
                                        )
                                        ShortInput(
                                            titleResID = R.string.email,
                                            value = state.email,
                                            changeValue = { newValue ->
                                                userInfoViewModel.handleIntent(
                                                    UserIntent.ChangeEmail(
                                                        newValue
                                                    )
                                                )
                                            },
                                            keyboardType = KeyboardType.Phone
                                        )
                                    }
                                }
                                if (user is UserTypes.IsCurrentMaster) {
                                    Column(
                                        Modifier.padding(horizontal = 8.dp)
                                    ) {
                                        Text(
                                            stringResource(R.string.master_data),
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                letterSpacing = 1.5.sp
                                            ),
                                            color = MaterialTheme.colorScheme.primary,
                                        )
                                        HorizontalDivider()
                                    }
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(8.dp),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            ShortInput(
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

                                            ShortWithComposableWOpadding(
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
                                                            userInfoViewModel.handleIntent(
                                                                UserIntent.CurrentMasterIntent.SaveSpecialization
                                                            )
                                                        }
                                                    )
                                                }
                                            )

                                            ShortInput(
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