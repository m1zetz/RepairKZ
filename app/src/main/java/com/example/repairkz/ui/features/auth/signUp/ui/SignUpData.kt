package com.example.repairkz.ui.features.auth.signUp.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.repairkz.Navigation.Routes.SIGN_UP_DATA
import com.example.repairkz.Navigation.Routes.SIGN_UP_EMAIL
import com.example.repairkz.R
import com.example.repairkz.common.enums.PhotoSourceEnum
import com.example.repairkz.common.ui.PhotoSourceBottomSheet
import com.example.repairkz.common.ui.UserPhoto
import com.example.repairkz.ui.features.UserInfo.UserIntent
import com.example.repairkz.ui.features.auth.signUp.SignUpIntent
import com.example.repairkz.ui.features.auth.signUp.SignUpViewModel

@Composable
fun SignUpData(signUpViewModel: SignUpViewModel, navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isCurrentScreen = currentBackStackEntry?.destination?.route == SIGN_UP_DATA

    BackHandler(enabled = isCurrentScreen) {
        signUpViewModel.handleIntent(SignUpIntent.ResetRegistrationData)
        navController.navigate(SIGN_UP_EMAIL) {
            popUpTo(SIGN_UP_EMAIL) { inclusive = true }
        }
    }
    SignUpLayout(
        signUpViewModel,
        navController
    ) { state ->
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
            UserPhoto(
                state.userInfo.photoUri?.toString(),
                changeAvatarIntent = {
                    signUpViewModel.handleIntent(SignUpIntent.OpenSheet)
                }
            )
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

@Composable
fun SignTextField(
    value: String,
    onValueChange: (newValue: String) -> Unit,
    errorMessage: Int?,
    leadingIcon: ImageVector? = null,
    placeholder: Int,
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        shape = MaterialTheme.shapes.medium,
        placeholder = {
            Text(
                stringResource(placeholder),
                style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
            )
        },
        leadingIcon = {
            leadingIcon?.let {
                Icon(leadingIcon, null)
            }
        },
        supportingText = {
            errorMessage?.let { stringResource ->
                Text(
                    stringResource(stringResource),
                    style = TextStyle(color = MaterialTheme.colorScheme.error)
                )
            }
        },
        isError = errorMessage != null
    )
}