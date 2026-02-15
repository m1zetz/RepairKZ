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
            val user = state.user
            Surface(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Cap(user.userPhotoUrl, user.firstName, user.lastName)
                    when(user){
                        is Master -> {
                            MasterBar(
                                onIntent = { intent ->
                                    userInfoViewModel.handleIntent(intent)
                                },
                                userId = state.clientId?:0,
                                masterId = user.userId,
                            )
                        }
                        else ->{

                        }
                    }

                }
            }

        }
    }
}