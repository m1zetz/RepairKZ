package com.example.repairkz.ui.features.settings

import com.example.repairkz.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.BackHand
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.repairkz.Activity.ActivityIntent
import com.example.repairkz.Activity.MainActivityViewModel
import com.example.repairkz.Navigation.Routes.userInfoRoute
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.ui.ProfileString
import com.example.repairkz.common.ui.StandartString

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel, activityViewModel: MainActivityViewModel, navController: NavController) {

    val uiState by settingsViewModel.uiState.collectAsState()
    val activityState by activityViewModel.state.collectAsState()

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
                        intent = { settingsViewModel.handleIntent(SettingIntent.toUserScreen(state.userData.id)) },

                    )
                    StandartString(
                        R.string.payment_system,
                        intent = {},
                        icon = Icons.Default.AddCard
                    )
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        onClick = {
                            activityViewModel.handleIntent(ActivityIntent.ChangeTheme(!activityState.isDarkTheme))
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {

                                Icon(Icons.Default.InvertColors, null)
                                Text(
                                    text = stringResource(R.string.themes),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Row(
                                modifier = Modifier,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Icon(Icons.Default.WbSunny, null)
                                Switch(
                                    checked = activityState.isDarkTheme,
                                    onCheckedChange = null
                                )
                                Icon(Icons.Default.Nightlight, null)
                            }
                        }

                    }
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



