package com.example.repairkz.ui.features.settings

import com.example.repairkz.R
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.Navigation.Routes.userInfoRoute
import com.example.repairkz.common.ui.ProfileString

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel, navController: NavController) {

    val uiState by settingsViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        settingsViewModel.settingEffectsChannel.collect {effect ->
            when(effect) {
                is SettingsEffects.NavigateToUserInfo -> {
                    navController.navigate(userInfoRoute(effect.userId))
                }
            }
        }
    }

    when(val state = uiState){
        is SettingsState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    ProfileString(
                        state.userData.userPhotoUrl,
                        intent = { settingsViewModel.handleIntent(SettingIntent.toUserScreen(state.userData.userId))},
                        name = "${state.userData.firstName} ${state.userData.lastName}",
                        description = "Статус -> ${state.userData.status}"
                    )
                    StandartString(
                        R.string.payment_system,
                        intent = {}
                    )
                    StandartString(
                        R.string.themes,
                        intent = {}
                    )
                    StandartString(
                        R.string.exit,
                        intent = {},
                        color = MaterialTheme.colorScheme.error
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


@Composable
fun StandartString(
    textR: Int,
    intent: () -> Unit,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        onClick = {
            intent()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(textR),
                color = color
            )
        }
    }
}

