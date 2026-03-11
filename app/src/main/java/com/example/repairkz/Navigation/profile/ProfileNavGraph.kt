package com.example.repairkz.Navigation.profile

import android.annotation.SuppressLint
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

@SuppressLint("UnrememberedGetBackStackEntry")
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
            getViewModel = {
                val entry = navController.getBackStackEntry(PROFILE_GROUP)
                hiltViewModel<UserInfoViewModel>(entry)
            }
        )

    }
}