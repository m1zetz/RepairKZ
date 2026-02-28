package com.example.repairkz.Navigation.profile

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.Navigation.Routes.PROFILE_GROUP
import com.example.repairkz.ui.features.CameraX.Camera
import com.example.repairkz.ui.features.CameraX.PhotoPreview
import com.example.repairkz.ui.features.UserInfo.UserInfo
import com.example.repairkz.ui.features.UserInfo.UserInfoViewModel
import com.example.repairkz.ui.features.UserInfo.UserIntent
import com.example.repairkz.ui.features.UserInfo.UserState

fun NavGraphBuilder.profileGraph(navController: NavController){
    navigation(
        startDestination = "${Routes.USERINFO}?userId={userId}",
        route = PROFILE_GROUP
    ) {

        composable(
            route = "${Routes.USERINFO}?userId={userId}"
        ) { nbse ->

            val parrentEntry = remember(nbse) {
                navController.getBackStackEntry(PROFILE_GROUP)
            }

            val userInfoViewModel: UserInfoViewModel = hiltViewModel(parrentEntry)

            UserInfo(userInfoViewModel, navController)
        }
        composable(Routes.CAMERA) {nbse ->
            val parrentEntry = remember(nbse) {
                navController.getBackStackEntry(PROFILE_GROUP)
            }
            val userInfoViewModel: UserInfoViewModel = hiltViewModel(parrentEntry)
            val context = LocalContext.current
            Camera(
                context = context,
                takeNewPhoto = { uri ->
                    uri?.let {
                        userInfoViewModel.handleIntent(
                            UserIntent.SelectedPhoto(it)
                        )
                        navController.popBackStack()
                    }

                }
            )
        }
        composable(Routes.PHOTO_PREVIEW) {it
            val context = LocalContext.current
            val parentEntry = remember(it) {
                try {
                    navController.getBackStackEntry(PROFILE_GROUP)
                } catch (e: Exception) {
                    it
                }
            }
            val userInfoViewModel: UserInfoViewModel = hiltViewModel(parentEntry)
            val state = userInfoViewModel.uiState.collectAsState()

            val currentState = state.value
            if (currentState is UserState.Success) {
                val uri = currentState.newAvatarData
                uri?.let{ nonNullUri ->
                    PhotoPreview(
                        context,
                        uri = nonNullUri
                    ) { uri ->
                        userInfoViewModel.handleIntent(
                            UserIntent.SelectedPhoto(
                                uri,
                            )
                        )
                        navController.popBackStack()
                    }
                }
            }
        }

    }
}