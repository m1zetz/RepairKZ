package com.example.repairkz.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.repairkz.ui.features.components.BottomNavigation
import com.example.repairkz.ui.features.home.HomeScreen
import com.example.repairkz.ui.features.main.MainIntent
import com.example.repairkz.ui.features.main.MainViewModel
import com.example.repairkz.ui.features.notifiacton.NotificationViewModel
import com.example.repairkz.ui.features.notifiacton.NotificationsScreen
import com.example.repairkz.ui.features.settings.SettingsScreen
import com.example.repairkz.ui.features.settings.SettingsViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainWindow(mainViewModel: MainViewModel, navController: NavController, notificationViewModel: NotificationViewModel, settingsViewModel: SettingsViewModel){
    val selectedItemIndex = mainViewModel.screenIndexState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RepairKZ") },
                navigationIcon = {

                },
                actions = {

                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        bottomBar = {
            BottomNavigation(
                selectedItemIndex = selectedItemIndex.value.selectedIndex,
                changeScreen = { index -> mainViewModel.handleIntent(MainIntent.ChangeScreen(index)) }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when(selectedItemIndex.value.selectedIndex){
                0 -> HomeScreen(navController = navController)
                1 -> NotificationsScreen(notificationViewModel)
                2 -> SettingsScreen(settingsViewModel, navController)
            }

        }

    }
}