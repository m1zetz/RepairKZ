package com.example.repairkz.ui.features.UserInfo


import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import com.example.repairkz.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width


import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

import com.example.repairkz.ui.features.components.Service
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.PhotoSourceEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.handlers.photoPickerHandler
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.ui.EnumDropDown
import com.example.repairkz.common.ui.PhotoSourceBottomSheet
import com.example.repairkz.common.ui.ShortInput
import com.example.repairkz.common.ui.ShortWithComposableWOpadding
import com.example.repairkz.ui.features.CameraX.CameraIntent
import com.example.repairkz.ui.features.CameraX.CameraViewModel
import com.example.repairkz.ui.features.CameraX.PhotoPreview
import com.example.repairkz.ui.features.profile.common.Cap


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun UserInfo(userInfoViewModel: UserInfoViewModel, navController: NavController) {
    val context = LocalContext.current
    val state by userInfoViewModel.uiState.collectAsStateWithLifecycle()
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
    val user = state.user
    if (!state.isLoading && user != null) {
        val scrollState = rememberScrollState()


        if (state.showSave) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(120.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black
                            )
                        )
                    )
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Scaffold(
                modifier = Modifier
                    .imePadding(),
            ) { paddingValues ->
                if (state.pendingUri != null) {
                    PhotoPreview(
                        context,
                        uri = state.pendingUri!!,
                        onDismissRequest = { userInfoViewModel.handleIntent(UserIntent.CancelPhoto) },
                    ) { uri ->
                        uri?.let {
                            userInfoViewModel.handleIntent(UserIntent.ConfirmPhoto(it))
                        }
                    }
                } else {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Cap(
                            businessCardData = BusinessCardData(
                                id = user.id,
                                photoUrl = user.userPhotoUrl ?: "",
                                firstName = user.firstName,
                                lastName = user.lastName,
                                isMe = false
                            ),
                            changeAvatarIntent = { userInfoViewModel.handleIntent(UserIntent.OpenSheet) },
                            isLoading = state.isPhotoSaving
                        )

                        //dewf
                        Column(
                            Modifier.padding(horizontal = 8.dp)
                                .fillMaxWidth()
                        ) {

                            Text(
                                stringResource(R.string.user_data),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    letterSpacing = 1.5.sp
                                ),
                                color = MaterialTheme.colorScheme.primary,
                            )

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
                                //номер телефона
                                ShortInput(
                                    titleResID = R.string.enter_number,
                                    value = state.numberDraft,
                                    changeValue = { newValue ->
                                        userInfoViewModel.handleIntent(
                                            UserIntent.ChangeNumber(
                                                newValue
                                            )
                                        )
                                    },
                                    keyboardType = KeyboardType.Phone
                                )
                                // эмейл
                                ShortInput(
                                    titleResID = R.string.email,
                                    value = state.emailDraft,
                                    changeValue = { email ->
                                        userInfoViewModel.handleIntent(
                                            UserIntent.ChangeEmail(
                                                email
                                            )
                                        )
                                    },
                                    keyboardType = KeyboardType.Email,
                                    readOnly = true
                                )
                                // город
                                ShortWithComposableWOpadding(
                                    R.string.city,
                                    {
                                        EnumDropDown(
                                            R.string.choice_city,
                                            state.cityDraft,
                                            CitiesEnum.entries,
                                            onSelect = { city ->
                                                userInfoViewModel.handleIntent(
                                                    UserIntent.ChangeCity(
                                                        city
                                                    )
                                                )

                                            }
                                        )
                                    }
                                )
                            }
                        }
                        // мастерские данные
                        if (user is Master) {
                            Column(
                                Modifier
                                    .padding(horizontal = 8.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    stringResource(R.string.master_data),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        letterSpacing = 1.5.sp
                                    ),
                                    color = MaterialTheme.colorScheme.primary,
                                )

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

                                    //описание
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
                                        singleLine = false,
                                        imeAction = ImeAction.Default

                                    )
                                    // спек
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

                                                }
                                            )
                                        }
                                    )

                                    // стаж опыт
                                    ShortInput(
                                        R.string.exp,
                                        R.string.exp,
                                        state.experienceDraft,
                                        { newValue ->
                                            userInfoViewModel.handleIntent(
                                                UserIntent.CurrentMasterIntent.ChangeExperience(
                                                    newValue
                                                )
                                            )
                                        },
                                        keyboardType = KeyboardType.Number
                                    )
                                }

                            }
                            Column(
                                Modifier
                                    .padding(horizontal = 8.dp)
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        stringResource(R.string.services),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            letterSpacing = 1.5.sp
                                        ),
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Button(
                                        onClick = {
                                            userInfoViewModel.handleIntent(UserIntent.CurrentMasterIntent.OpenCreate)
                                        }
                                    ) {
                                        Text("Создать")
                                    }
                                }

                            }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(IntrinsicSize.Min)
                                            .padding(10.dp),

                                        ) {
                                        Text(
                                            stringResource(R.string.service_name),
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(end = 8.dp)
                                                .align(Alignment.Top),
                                            textAlign = TextAlign.Center,
                                            fontWeight = FontWeight.Medium
                                        )
                                        VerticalDivider(
                                            Modifier
                                                .fillMaxHeight()
                                                .width(1.dp),
                                            color = MaterialTheme.colorScheme.outlineVariant
                                        )
                                        Text(
                                            stringResource(R.string.price_in_tenge),
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 8.dp)
                                                .align(Alignment.Top),
                                            textAlign = TextAlign.Center,
                                            color = MaterialTheme.colorScheme.primary
                                        )


                                    }
                                    HorizontalDivider(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp)
                                    )
                                    user.services.sortedBy {
                                        it.position
                                    }.forEach { item ->
                                        Log.d("UI", "rendering service: ${item.service}")

                                        Service(
                                            item,
                                            onDelete = {
                                                if (item.id != null) {
                                                    userInfoViewModel.handleIntent(
                                                        UserIntent.CurrentMasterIntent.DeleteService(
                                                            item.id
                                                        )
                                                    )
                                                } else {
                                                }
                                            },
                                            onEdit = {

                                            }
                                        )
                                    }
                                    Spacer(Modifier.size(8.dp))
                                }

                            }
                            AnimatedVisibility(
                                visible = state.showSave,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    onClick = {
                                        userInfoViewModel.handleIntent(
                                            UserIntent.SaveChanges
                                        )
                                    }) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(
                                            8.dp,
                                            Alignment.CenterHorizontally
                                        ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(stringResource(R.string.save_changes))
                                        if (state.isSaving) {
                                            CircularProgressIndicator(
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.size(80.dp))


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
        if (state.showCreate) {
            ModalBottomSheet(
                onDismissRequest = {
                    userInfoViewModel.handleIntent(UserIntent.CurrentMasterIntent.CloseCreate)
                },
            ) {
                Column(
                    Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = state.serviceDraft,
                        onValueChange = { newValue ->
                            userInfoViewModel.handleIntent(
                                UserIntent.CurrentMasterIntent.ChangeServiceDraft(
                                    newValue
                                )
                            )
                        }
                    )
                    TextField(
                        value = state.priceDraft,
                        onValueChange = { newValue ->
                            userInfoViewModel.handleIntent(
                                UserIntent.CurrentMasterIntent.ChangePriceDraft(
                                    newValue
                                )
                            )
                        }
                    )
                    Button(
                        onClick = {
                            userInfoViewModel.handleIntent(UserIntent.CurrentMasterIntent.CreateService)
                        }
                    ) {
                        Text("Создать")
                    }
                }
            }
        }
    }
}