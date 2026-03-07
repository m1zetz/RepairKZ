package com.example.repairkz.Navigation.registration

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
import com.example.repairkz.Navigation.Routes.REG_GROUP
import com.example.repairkz.Navigation.Routes.SIGN_UP_CODE
import com.example.repairkz.Navigation.Routes.SIGN_UP_DATA
import com.example.repairkz.Navigation.Routes.SIGN_UP_EMAIL
import com.example.repairkz.ui.features.CameraX.Camera
import com.example.repairkz.ui.features.CameraX.PhotoPreview
import com.example.repairkz.ui.features.UserInfo.UserInfo
import com.example.repairkz.ui.features.UserInfo.UserInfoViewModel
import com.example.repairkz.ui.features.UserInfo.UserIntent
import com.example.repairkz.ui.features.UserInfo.UserState
import com.example.repairkz.ui.features.auth.signUp.SignUpViewModel
import com.example.repairkz.ui.features.auth.signUp.ui.SignUpCode
import com.example.repairkz.ui.features.auth.signUp.ui.SignUpData
import com.example.repairkz.ui.features.auth.signUp.ui.SignUpEmail

fun NavGraphBuilder.registrationGraph(navController: NavController){
    navigation(
        startDestination = SIGN_UP_EMAIL,
        route = REG_GROUP
    ) {
        composable(
            route = SIGN_UP_EMAIL
        ) { nbse ->

            val parrentEntry = remember(nbse) {
                navController.getBackStackEntry(REG_GROUP)
            }
            val signUpViewModel: SignUpViewModel = hiltViewModel(parrentEntry)
            SignUpEmail(signUpViewModel,navController)
        }
        composable(
            route = SIGN_UP_CODE
        ) { nbse ->
            val parrentEntry = remember(nbse) {
                navController.getBackStackEntry(REG_GROUP)
            }
            val signUpViewModel: SignUpViewModel = hiltViewModel(parrentEntry)
            SignUpCode(signUpViewModel, navController)
        }
        composable(
            route = SIGN_UP_DATA
        ) { nbse ->
            val parrentEntry = remember(nbse) {
                navController.getBackStackEntry(REG_GROUP)
            }
            val signUpViewModel: SignUpViewModel = hiltViewModel(parrentEntry)
            SignUpData(signUpViewModel,navController)

        }

    }
}