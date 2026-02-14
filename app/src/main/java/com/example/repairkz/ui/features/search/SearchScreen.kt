package com.example.repairkz.ui.features.search

import com.example.repairkz.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.Navigation.Routes.userInfoRoute
import com.example.repairkz.common.enums.Cities
import com.example.repairkz.common.ui.ProfileString
import com.example.repairkz.common.ui.ShortInputCard
import com.example.repairkz.ui.features.notifiacton.NotificationIntent


@SuppressLint("LocalContextGetResourceValueCall", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, searchViewModel: SearchViewModel) {
    val currentState by searchViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    LaunchedEffect(Unit) {
        searchViewModel.handleIntent(SearchIntents.GetData)
    }

    LaunchedEffect(Unit) {
        currentState.initialPatternResId?.let { resId ->
            if (resId != 0) {
                val pattern = context.getString(resId)
                searchViewModel.handleIntent(SearchIntents.ChangeText(pattern))
            }

        }
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
            trailingIcon = {
                IconButton(
                    onClick = {
                        searchViewModel.handleIntent(SearchIntents.OpenFilters)
                    }
                ) {
                    Icon(
                        Icons.Default.FilterAlt,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.statusBarsPadding()
        ) {
            when (val state = currentState.result) {
                is SearchResult.Success -> {
                    Text(
                        "Результаты для: ${currentState.query}",
                        modifier = Modifier.padding(16.dp)
                    )
                    state.masters.let { listOfMaters ->
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(listOfMaters) { master ->
                                ProfileString(
                                    master.avatarURL,
                                    master.masterName,
                                    master.masterSpecialization,
                                    intent = {
                                        searchViewModel.handleIntent(
                                            SearchIntents.NavigateToUserInfo(
                                                master.id
                                            )
                                        )
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

        if (currentState.isFiltersSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { searchViewModel.handleIntent(SearchIntents.CloseFilters) },
                sheetState = sheetState
            ) {
                Filters(
                    currentState,
                    onIntent = { intents -> searchViewModel.handleIntent(intents) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filters(searchState: SearchUiState, onIntent: (SearchIntents) -> Unit) {
    ShortInputCard(
        R.string.experienceInYears,
        searchState.filterData.experienceInYears.toString(),
        { newValue ->
            onIntent(SearchIntents.FilterActions(FilterIntents.UpdateYears(newValue.toInt())))
        }
    )
    ShortInputCard(
        R.string.detail_descriptions,
        searchState.filterData.detailDescriptions.toString(),
        { newValue ->
            onIntent(SearchIntents.FilterActions(FilterIntents.UpdateDetailDescriptions(newValue)))
        }
    )

    var selectedOption by remember { mutableStateOf(Cities.ALMATY) }
    var cityMenuIsOpen by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = cityMenuIsOpen,
        onExpandedChange = {
            cityMenuIsOpen = !cityMenuIsOpen
        }
    ) {
        TextField(
            value = stringResource(selectedOption.resID),
            readOnly = true,
            onValueChange = {},
            modifier = Modifier.menuAnchor()
        )
        DropdownMenu(
            expanded = cityMenuIsOpen,
            onDismissRequest = {
                cityMenuIsOpen = false
            }
        ) {
            Cities.entries.forEachIndexed { index, city ->
                DropdownMenuItem(
                    text = { Text(stringResource(city.resID)) },
                    onClick = {
                        selectedOption = city
                        cityMenuIsOpen = false
                    }
                )
            }
        }
    }


}


