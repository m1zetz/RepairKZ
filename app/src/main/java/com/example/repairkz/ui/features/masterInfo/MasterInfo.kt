package com.example.repairkz.ui.features.masterInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.R
import com.example.repairkz.ui.features.UserInfo.BusinessCardData
import com.example.repairkz.ui.features.components.ServicesTable
import com.example.repairkz.ui.features.profile.common.Cap
import com.example.repairkz.ui.features.profile.master.MasterBar

@Composable
fun MasterInfo(masterInfoViewModel: MasterInfoViewModel, navController: NavController) {

    LaunchedEffect(Unit) {
        masterInfoViewModel.channel.collect {  effect ->
            when(effect){
                is MasterProfileEffect.NavigateToOrderReg -> {
                    navController.navigate(Routes.orderRegRoute(effect.masterId))
                }
            }
        }
    }

    val state by masterInfoViewModel.state.collectAsState()
    val scrollState = rememberScrollState()
    val master = state.master
    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Scaffold(
            modifier = Modifier
                .imePadding(),
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                master?.let{
                    Cap(
                        businessCardData = BusinessCardData(
                            id = master.id,
                            photoUrl = master.userPhotoUrl?:"",
                            firstName = master.firstName,
                            lastName = master.lastName,
                            isMe = false
                        )
                    )
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        MasterBar(
                            doOrder = {
                                masterInfoViewModel.handleIntent(MasterProfileIntent.DoOrder)
                            },
                            addToFavorites = {

                            },
                            doReport = {

                            }
                        )
                        val services = state.master?.services

                        Column(
                            Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                stringResource(R.string.services),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    letterSpacing = 1.5.sp
                                ),
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        if(services != null && services.isNotEmpty()){
                            ServicesTable(
                                services = services
                            )
                        }


                    }
                }

            }
        }
    }

}