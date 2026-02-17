package com.example.repairkz.ui.features.UserInfo

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User
import com.example.repairkz.ui.features.profile.common.Cap
import com.example.repairkz.ui.features.profile.master.MasterBar

@Composable
fun UserInfo(userInfoViewModel: UserInfoViewModel){

    val uiState = userInfoViewModel.uiState.collectAsState()

    when(val state = uiState.value){
        is UserState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }

        is UserState.Error -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(state.message)
            }
        }
        is UserState.Success -> {
            val user = state.userTypes
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Cap(user.commonInfo.photoUrl, user.commonInfo.firstName, user.commonInfo.lastName)
                    when(user){
                        is UserTypes.IsCurrentMaster -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                Text("Я мастер")
                            }
                        }
                        is UserTypes.IsCurrentUser -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                Text("Я клиент")
                            }
                        }
                        is UserTypes.IsOtherMaster -> {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ){
                                Text(user.master.firstName)
                            }
                        }
                    }

                }
            }

        }
    }
}