package com.example.repairkz.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.repairkz.ui.features.components.BottomNavigation
import com.example.repairkz.ui.features.home.HomeScreen
import com.example.repairkz.ui.features.main.MainIntent
import com.example.repairkz.ui.features.main.MainViewModel
import com.example.repairkz.ui.features.notifiacton.NotificationsScreen
import com.example.repairkz.ui.features.settings.SettingsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainWindow(mainViewModel: MainViewModel, navController: NavController){
    val selectedItemIndex = mainViewModel.screenIndexState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("RepairKZ") },
                navigationIcon = {

                },
                actions = {

                }
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
                1 -> NotificationsScreen()
                2 -> SettingsScreen()
            }

        }

    }
}