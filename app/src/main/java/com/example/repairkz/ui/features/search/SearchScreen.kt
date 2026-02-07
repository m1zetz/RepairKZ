package com.example.repairkz.ui.features.search

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController){
    val searchViewModel: SearchViewModel = hiltViewModel()
    val state = searchViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(searchViewModel.searchEffectsChannel) {
        searchViewModel.searchEffectsChannel.collect { effect ->
            when(effect){
                is SearchEffects.NavigateBack -> navController.popBackStack()
            }
        }
    }

    Scaffold {
        SearchBar(
            query = state.value.query,
            onQueryChange = { newText ->
                searchViewModel.handleIntent(SearchIntents.ChangeText(newText))
            },
            onSearch = {
//                    searchViewModel.handleIntent(SearchIntents.SetActive(false))
            },
            active = true,
            onActiveChange = {
//                    searchViewModel.handleIntent(SearchIntents.SetActive(it))
            },
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
        ){
            Text("Результаты для: ${state.value.query}", modifier = Modifier.padding(16.dp))
        }

    }
}