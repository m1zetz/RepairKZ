package com.example.repairkz.ui.features.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.Navigation.Routes.userInfoRoute
import com.example.repairkz.common.ui.ProfileString
import com.example.repairkz.ui.features.notifiacton.NotificationIntent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, searchViewModel: SearchViewModel) {
    val currentState by searchViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        searchViewModel.handleIntent(SearchIntents.GetData)
    }

    LaunchedEffect(searchViewModel.searchEffectsChannel) {
        searchViewModel.searchEffectsChannel.collect { effect ->
            when (effect) {
                is SearchEffects.NavigateBack -> navController.popBackStack()
                is SearchEffects.NavigateToUserInfo -> {
                    navController.navigate(userInfoRoute(effect.id))
                }
            }
        }

    }


    Scaffold(Modifier.fillMaxSize()) {
        SearchBar(
            query = currentState.query,
            onQueryChange = { newText ->
                searchViewModel.handleIntent(SearchIntents.ChangeText(newText))
            },

            onSearch = {},
            active = true,
            onActiveChange = {},
            leadingIcon = {
                IconButton(
                    onClick = {
                        searchViewModel.handleIntent(SearchIntents.NavigateToBack)
                    }
                ) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            },
            modifier = Modifier.statusBarsPadding()
        ) {
            when(val state = currentState.result){
                is SearchResult.Success ->{
                    Text("Результаты для: ${currentState.query}", modifier = Modifier.padding(16.dp))
                    state.masters.let { listOfMaters ->
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(listOfMaters){master ->
                                ProfileString(master.avatarURL, master.masterName, master.masterSpecialization, intent = {
                                    searchViewModel.handleIntent(SearchIntents.NavigateToUserInfo(master.id))
                                })
                            }
                        }
                    }
                }

                is SearchResult.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.message)
                    }
                }
                SearchResult.Idle -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Введите имя мастера или специализацию"
                        )
                    }
                }
                SearchResult.Loading -> {
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

    }

}

