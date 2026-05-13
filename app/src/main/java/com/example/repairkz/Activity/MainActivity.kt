package com.example.repairkz.Activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.Navigation.Routes.PROFILE_GROUP
import com.example.repairkz.Navigation.photo.photoGraph
import com.example.repairkz.Navigation.profile.profileGraph
import com.example.repairkz.Navigation.registration.registrationGraph
import com.example.repairkz.ui.MainWindow
import com.example.repairkz.ui.features.UserInfo.UserInfo
import com.example.repairkz.ui.features.UserInfo.UserInfoViewModel
import com.example.repairkz.ui.features.auth.signIn.SignIn
import com.example.repairkz.ui.features.auth.signIn.SignInViewModel
import com.example.repairkz.ui.features.main.MainViewModel
import com.example.repairkz.ui.features.masterInfo.MasterInfo
import com.example.repairkz.ui.features.masterInfo.MasterInfoViewModel
import com.example.repairkz.ui.features.notifiacton.NotificationViewModel
import com.example.repairkz.ui.features.search.SearchScreen
import com.example.repairkz.ui.features.search.SearchViewModel
import com.example.repairkz.ui.features.search.orderReg.OrderRegistration
import com.example.repairkz.ui.features.settings.SettingsViewModel
import com.example.repairkz.ui.theme.RepairkzTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.Companion.light(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        setContent {
            val viewModel: MainActivityViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()
            splashscreen.setKeepOnScreenCondition { state.startDestination == null }
            WindowCompat.setDecorFitsSystemWindows(window, false)

            RepairkzTheme(darkTheme = state.isDarkTheme) {
                val navController = rememberNavController()
                Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
                    if (state.startDestination != null) {
                        NavHost(
                            navController = navController,
                            startDestination = if (state.startDestination == StartDestination.MainWindow) Routes.MAIN_WINDOW else Routes.SIGN_IN
                        ) {
                            registrationGraph(navController)
                            composable(
                                route = Routes.USER_INFO
                            ) {
                                val userInfoViewModel: UserInfoViewModel = hiltViewModel()

                                UserInfo(userInfoViewModel, navController)
                            }
                            photoGraph(navController)
                            composable(Routes.SIGN_IN) {
                                val signInViewModel: SignInViewModel = hiltViewModel()
                                SignIn(signInViewModel, navController)
                            }
                            composable(Routes.MAIN_WINDOW) {
                                val mainViewModel: MainViewModel = hiltViewModel()
                                val notificationViewModel: NotificationViewModel = hiltViewModel()
                                val settingsViewModel: SettingsViewModel = hiltViewModel()
                                MainWindow(
                                    viewModel,
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
                                        type = NavType.Companion.IntType
                                        defaultValue = 0
                                    }
                                )
                            ) {
                                val searchViewModel: SearchViewModel = hiltViewModel()
                                SearchScreen(navController, searchViewModel)
                            }
                            composable(
                                route = "${Routes.ORDER_REG}?id={masterId}",
                                arguments = listOf(
                                    navArgument("masterId") {
                                        type = NavType.Companion.LongType
                                        defaultValue = 0
                                    }
                                )
                            ){
                                OrderRegistration(navController = navController)
                            }
                            composable(
                                route = "${Routes.MASTER_INFO}?userId={userId}",
                                arguments = listOf(
                                    navArgument("userId") {
                                        type = NavType.Companion.LongType
                                        defaultValue = 0
                                    }
                                )
                            ){
                                val masterInfoViewModel: MasterInfoViewModel = hiltViewModel()
                                MasterInfo(masterInfoViewModel)
                            }
                        }
                    }
                }

            }
        }
    }
}
