package com.example.repairkz.ui.features.auth.signIn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.R


@Composable
fun SignIn(signInViewModel: SignInViewModel, navController: NavController) {
    val state by signInViewModel.signInState.collectAsState()

    LaunchedEffect(Unit) {
        signInViewModel.channel.collect {effect ->
            when(effect){
                SignInEffects.NavigateToRegistration -> {
                    navController.navigate(Routes.SIGN_UP_EMAIL)
                }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

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
                stringResource(R.string.welcome),
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.size(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                value = state.email,
                onValueChange = { newValue ->
                    signInViewModel.handleIntent(SignInIntent.ChangeEmail(newValue))
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

            var passwordVisible by rememberSaveable { mutableStateOf(false) }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                value = state.password,
                onValueChange = { newValue ->
                    signInViewModel.handleIntent(SignInIntent.ChangePassword(newValue))
                },
                shape = MaterialTheme.shapes.medium,
                placeholder = {
                    Text(
                        stringResource(R.string.password),
                        style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                    )
                },
                leadingIcon = {
                    Icon(Icons.Default.Key, null)
                },
                supportingText = {
                    state.passwordError?.let { stringResource ->
                        Text(
                            stringResource(stringResource),
                            style = TextStyle(color = MaterialTheme.colorScheme.error)
                        )
                    }
                },
                isError = state.passwordError != null,
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, null)
                    }
                }
            )
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    signInViewModel.handleIntent(SignInIntent.SignIn(state.email, state.password))
                }
            ) {
                Text(stringResource(R.string.sign_in))
            }

            TextButton(
                onClick = {
                    signInViewModel.handleIntent(SignInIntent.NavigateToRegistration)
                }
            ) {
                Text(stringResource(R.string.to_sign_up))
            }

        }
    }
}

