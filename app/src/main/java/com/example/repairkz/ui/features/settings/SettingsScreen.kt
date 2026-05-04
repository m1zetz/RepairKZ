package com.example.repairkz.ui.features.settings

import android.annotation.SuppressLint
import com.example.repairkz.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.repairkz.Activity.ActivityIntent
import com.example.repairkz.Activity.MainActivityViewModel
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.Navigation.Routes.userInfoRoute
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.ui.ProfileString
import com.example.repairkz.common.ui.StandartString
import com.example.repairkz.domain.errors.toMessage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel, activityViewModel: MainActivityViewModel, navController: NavController, snackbarHostState: SnackbarHostState) {

    val context = LocalContext.current
    val uiState by settingsViewModel.uiState.collectAsState()
    val activityState by activityViewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        settingsViewModel.settingEffectsChannel.collect { effect ->
            when (effect) {
                is SettingsEffect.NavigateToUserInfo -> {
                    navController.navigate(userInfoRoute(effect.id))
                }

                SettingsEffect.NavigateToLogin -> {
                    navController.navigate(Routes.SIGN_IN_SCREEN){
                        popUpTo(0) { inclusive = true }
                    }
                }

                is SettingsEffect.ShowError -> snackbarHostState.showSnackbar(effect.error.toMessage(context))
            }
        }
    }
    when (val state = uiState) {
        is SettingsState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileString(
                    state.userData,
                    descriptionPrefix = stringResource(R.string.your_status),
                    intent = { settingsViewModel.handleIntent(SettingIntent.ToUserScreen(state.userData.id)) },

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

                val switchConfig = when(state.currentStatus){
                    StatusOfUser.CLIENT -> {
                        ChangeStatusConfig(
                            Icons.Default.Build,
                            R.string.become_a_master,
                            StatusOfUser.MASTER
                        )
                    }
                    StatusOfUser.MASTER -> {
                        ChangeStatusConfig(
                            Icons.Default.BackHand,
                            R.string.become_a_client,
                            StatusOfUser.CLIENT
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    onClick = {
                        settingsViewModel.handleIntent(SettingIntent.SwitchStatus(switchConfig.status))
                    }
                ){
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
                        ){
                            Icon(switchConfig.icon, null)
                            Text(
                                text = stringResource(switchConfig.textRes),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            if(state.isChangeStatusLoading){
                                CircularProgressIndicator()
                            }
                        }

                    }

                }

                StandartString(
                    R.string.exit,
                    intent = {settingsViewModel.handleIntent(SettingIntent.Exit)},
                    color = MaterialTheme.colorScheme.error,
                    icon = Icons.Default.ExitToApp
                )

            }
        }

        is SettingsState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(state.error.toMessage(context))
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



