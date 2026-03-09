package com.example.repairkz.ui.features.auth.signUp.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes.SIGN_UP_EMAIL
import com.example.repairkz.ui.features.auth.signUp.SignUpIntent
import com.example.repairkz.ui.features.auth.signUp.SignUpViewModel

@Composable
fun SignUpData(signUpViewModel: SignUpViewModel,navController: NavController){
    BackHandler {
        signUpViewModel.handleIntent(SignUpIntent.ResetRegistrationData)
        navController.navigate(SIGN_UP_EMAIL){
            popUpTo(SIGN_UP_EMAIL){inclusive = true}
        }
    }
    SignUpLayout(
        signUpViewModel,
        navController
    ){ state ->

    }
}