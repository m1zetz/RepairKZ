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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes.SIGN_UP_CODE
import com.example.repairkz.R
import com.example.repairkz.ui.features.auth.signUp.SignUpEffect
import com.example.repairkz.ui.features.auth.signUp.SignUpIntent
import com.example.repairkz.ui.features.auth.signUp.SignUpViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpEmail(signUpViewModel: SignUpViewModel, navController: NavController){
    SignUpLayout(
        signUpViewModel,
        navController
    ){ state ->
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

            Text(
                stringResource(R.string.registration),
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.size(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                value = state.email,
                onValueChange = { newValue ->
                    signUpViewModel.handleIntent(SignUpIntent.ChangeEmail(newValue))
                },
                shape = MaterialTheme.shapes.medium,
                placeholder = {
                    Text(
                        stringResource(R.string.email_example),
                        style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                    )
                },
                leadingIcon = {
                    Icon(Icons.Default.Email, null)
                },
                supportingText = {
                    state.emailError?.let { stringResource ->
                        Text(
                            stringResource(stringResource),
                            style = TextStyle(color = MaterialTheme.colorScheme.error)
                        )
                    }
                },
                isError = state.emailError != null
            )
            if(!state.isLoading){
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        signUpViewModel.handleIntent(SignUpIntent.SendEmail(state.email))
                    }
                ) {
                    Text(stringResource(R.string.get_code))
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }


}