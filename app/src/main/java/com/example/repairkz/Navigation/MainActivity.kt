package com.example.repairkz.Navigation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.repairkz.Navigation.Routes.DETAILS

import com.example.repairkz.ui.MainWindow
import com.example.repairkz.ui.features.UserInfo.UserInfo
import com.example.repairkz.ui.features.UserInfo.UserInfoViewModel
import com.example.repairkz.ui.features.search.SearchScreen
import com.example.repairkz.ui.theme.RepairkzTheme
import com.example.repairkz.ui.features.home.HomeViewModel
import com.example.repairkz.ui.features.main.MainViewModel
import com.example.repairkz.ui.features.notifiacton.NotificationViewModel
import com.example.repairkz.ui.features.search.SearchViewModel
import com.example.repairkz.ui.features.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RepairkzTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.MAIN_WINDOW
                ) {
                    composable(Routes.MAIN_WINDOW) {
                        val mainViewModel: MainViewModel = hiltViewModel()
                        val notificationViewModel: NotificationViewModel = hiltViewModel()
                        val settingsViewModel: SettingsViewModel = hiltViewModel()
                        MainWindow(mainViewModel,navController, notificationViewModel, settingsViewModel)
                    }
                    composable(
                        route = "${Routes.SEARCH}?pattern={pattern}"
                    ) {
                        val searchViewModel: SearchViewModel = hiltViewModel()
                        SearchScreen(navController, searchViewModel)
                    }
                    composable(
                        route = "${Routes.USERINFO}?userId={userId}"
                    ){
                        val userInfoViewModel: UserInfoViewModel = hiltViewModel()
                        UserInfo(userInfoViewModel)
                    }
                }
            }
        }
    }
}