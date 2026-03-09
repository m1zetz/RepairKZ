package com.example.repairkz.ui.features.auth.signUp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes.SIGN_UP_CODE
import com.example.repairkz.R
import com.example.repairkz.ui.features.auth.signIn.SignInIntent
import com.example.repairkz.ui.features.auth.signUp.SignUpEffect
import com.example.repairkz.ui.features.auth.signUp.SignUpIntent
import com.example.repairkz.ui.features.auth.signUp.SignUpViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpCode(signUpViewModel: SignUpViewModel, navController: NavController) {
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

            Text(
                stringResource(R.string.confirmation),
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.size(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                value = state.code,
                onValueChange = { newValue ->
                    signUpViewModel.handleIntent(SignUpIntent.ChangeCode(newValue))
                },
                shape = MaterialTheme.shapes.medium,
                placeholder = {
                    Text(
                        "000000",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                alpha = 0.5f
                            )
                        )
                    )
                },
                leadingIcon = {
                    Icon(Icons.Default.Lock, null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                supportingText = {
                    state.codeError?.let { stringResource ->
                        Text(
                            stringResource(stringResource),
                            style = TextStyle(color = MaterialTheme.colorScheme.error)
                        )
                    }
                }
            )
            if (!state.isLoading) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        signUpViewModel.handleIntent(SignUpIntent.SendCode(state.code))
                    }
                ) {
                    Text(stringResource(R.string.send_code))
                }
                if (state.isCodeSent)
                    TextButton(
                        onClick = {
                            signUpViewModel.handleIntent(SignUpIntent.SendEmail(state.email))
                        },
                        enabled = state.timerSeconds == 0
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(stringResource(R.string.resend_email))
                            Spacer(modifier = Modifier.size(8.dp))
                            if (state.timerSeconds != 0){
                                Text(state.timerSeconds.toString())
                            }

                        }

                    }
            } else {
                CircularProgressIndicator()
            }
        }
    }


}