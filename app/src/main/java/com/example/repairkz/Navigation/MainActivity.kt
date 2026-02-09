package com.example.repairkz.Navigation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.repairkz.Navigation.Routes.DETAILS

import com.example.repairkz.ui.MainWindow
import com.example.repairkz.ui.features.search.SearchScreen
import com.example.repairkz.ui.theme.RepairkzTheme
import com.example.repairkz.ui.features.home.HomeViewModel
import com.example.repairkz.ui.features.main.MainViewModel
import com.example.repairkz.ui.features.notifiacton.NotificationViewModel
import com.example.repairkz.ui.features.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RepairkzTheme {
                val mainViewModel: MainViewModel = hiltViewModel()
                val notificationViewModel: NotificationViewModel = hiltViewModel()
                val searchViewModel: SearchViewModel = hiltViewModel()
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.MAIN_WINDOW
                ) {
                    composable(Routes.MAIN_WINDOW) {
                        MainWindow(mainViewModel,navController, notificationViewModel)
                    }
                    composable(
                        route = "${Routes.SEARCH}?pattern={pattern}"
                    ) {
                        SearchScreen(navController, searchViewModel)
                    }
                }
            }
        }
    }
}