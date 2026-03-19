package com.example.repairkz.ui.features.auth.signUp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.Navigation.Routes.MAIN_WINDOW
import com.example.repairkz.Navigation.Routes.SIGN_UP_CODE
import com.example.repairkz.Navigation.Routes.SIGN_UP_DATA
import com.example.repairkz.R
import com.example.repairkz.common.enums.PhotoSourceEnum
import com.example.repairkz.common.handlers.photoPickerHandler
import com.example.repairkz.ui.features.UserInfo.UserIntent
import com.example.repairkz.ui.features.auth.signUp.SignUpEffect
import com.example.repairkz.ui.features.auth.signUp.SignUpIntent
import com.example.repairkz.ui.features.auth.signUp.SignUpState
import com.example.repairkz.ui.features.auth.signUp.SignUpViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpLayout(
    signUpViewModel: SignUpViewModel,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    content: @Composable (SignUpState) -> Unit,

) {
    val state by signUpViewModel.state.collectAsState()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            content(state)
        }
    }
}