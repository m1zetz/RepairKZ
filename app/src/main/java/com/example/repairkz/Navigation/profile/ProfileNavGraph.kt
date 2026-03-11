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
import com.example.repairkz.Navigation.photoGraph
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
        photoGraph(
            navController = navController,
            parentRoute = PROFILE_GROUP,
            route = Routes.PROFILE_PHOTO_GROUP,
            getViewModel = { parentEntry -> hiltViewModel<UserInfoViewModel>(parentEntry) }
        )

    }
}