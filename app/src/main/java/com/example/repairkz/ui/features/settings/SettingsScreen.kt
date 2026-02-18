package com.example.repairkz.ui.features.settings

import com.example.repairkz.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.BackHand
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Money
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes.userInfoRoute
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.ui.ProfileString
import com.example.repairkz.common.ui.StandartString

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel, navController: NavController) {

    val uiState by settingsViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        settingsViewModel.settingEffectsChannel.collect { effect ->
            when (effect) {
                is SettingsEffects.NavigateToUserInfo -> {
                    navController.navigate(userInfoRoute(effect.id))
                }
            }
        }
    }

    when (val state = uiState) {
        is SettingsState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    ProfileString(
                        state.userData,
                        descriptionPrefix = stringResource(R.string.your_status),
                        intent = { settingsViewModel.handleIntent(SettingIntent.toUserScreen(state.userData.userId)) },

                    )
                    StandartString(
                        R.string.payment_system,
                        intent = {},
                        icon = Icons.Default.AddCard
                    )
                    StandartString(
                        R.string.themes,
                        intent = {},
                        icon = Icons.Default.InvertColors
                    )
                    when(state.currentStatus){
                        StatusOfUser.CLIENT -> {
                            StandartString(
                                R.string.become_a_master,
                                intent = {
                                    settingsViewModel.handleIntent(SettingIntent.SwitchStatus(StatusOfUser.MASTER))
                                },
                                color = MaterialTheme.colorScheme.primary,
                                icon = Icons.Default.Build
                            )
                        }
                        StatusOfUser.MASTER -> {
                            StandartString(
                                R.string.become_a_client,
                                intent = {
                                    settingsViewModel.handleIntent(SettingIntent.SwitchStatus(StatusOfUser.CLIENT))
                                },
                                color = MaterialTheme.colorScheme.primary,
                                icon = Icons.Default.BackHand
                            )
                        }
                    }

                    StandartString(
                        R.string.exit,
                        intent = {},
                        color = MaterialTheme.colorScheme.error,
                        icon = Icons.Default.ExitToApp
                    )

                }

            }
        }

        is SettingsState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(state.message)
            }
        }

        SettingsState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    }
}



