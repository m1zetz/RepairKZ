package com.example.repairkz.Navigation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.repairkz.Navigation.profile.profileGraph
import com.example.repairkz.Navigation.registration.registrationGraph

import com.example.repairkz.ui.MainWindow
import com.example.repairkz.ui.features.search.SearchScreen
import com.example.repairkz.ui.theme.RepairkzTheme
import com.example.repairkz.ui.features.main.MainViewModel
import com.example.repairkz.ui.features.notifiacton.NotificationViewModel
import com.example.repairkz.ui.features.search.SearchViewModel
import com.example.repairkz.ui.features.settings.SettingsViewModel
import com.example.repairkz.ui.features.auth.signIn.SignIn
import com.example.repairkz.ui.features.auth.signIn.SignInViewModel
import com.example.repairkz.ui.features.auth.signUp.ui.SignUpEmail
import com.example.repairkz.ui.features.auth.signUp.SignUpViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedGetBackStackEntry")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        setContent {

            RepairkzTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.SIGN_IN
                ) {
                    composable(Routes.SIGN_IN){
                        val signInViewModel: SignInViewModel = hiltViewModel()
                        SignIn(signInViewModel,navController)
                    }
                    registrationGraph(navController)
                    composable(Routes.MAIN_WINDOW) {
                        val mainViewModel: MainViewModel = hiltViewModel()
                        val notificationViewModel: NotificationViewModel = hiltViewModel()
                        val settingsViewModel: SettingsViewModel = hiltViewModel()
                        MainWindow(
                            mainViewModel,
                            navController,
                            notificationViewModel,
                            settingsViewModel
                        )
                    }
                    composable(
                        route = "${Routes.SEARCH}?pattern={pattern}",
                        arguments = listOf(
                            navArgument("pattern") {
                                type = NavType.IntType
                                defaultValue = 0
                            }
                        )
                    ) {
                        val searchViewModel: SearchViewModel = hiltViewModel()
                        SearchScreen(navController, searchViewModel)
                    }
                    profileGraph(navController)

                }
            }
        }
    }
}