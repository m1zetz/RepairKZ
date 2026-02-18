package com.example.repairkz.ui.features.search

import com.example.repairkz.R
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterAltOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.repairkz.Navigation.Routes.userInfoRoute
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.ui.EnumDropDown
import com.example.repairkz.common.ui.ProfileString
import com.example.repairkz.common.ui.ShortInputCard
import com.example.repairkz.common.ui.ShortWithComposableCard


@SuppressLint("LocalContextGetResourceValueCall", "UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, searchViewModel: SearchViewModel) {
    val currentState by searchViewModel.uiState.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
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
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(modifier = Modifier.fillMaxSize()) {
            SearchBar(
                query = currentState.query,
                onQueryChange = { newText ->
                    searchViewModel.handleIntent(SearchIntents.ChangeText(newText))
                    searchViewModel.handleIntent(SearchIntents.GetData)
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
                placeholder = {Text(stringResource(R.string.find_by_name))},
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
                                        master,
                                        intent = {
                                            searchViewModel.handleIntent(
                                                SearchIntents.NavigateToUserInfo(
                                                    master.userId
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filters(searchState: SearchUiState, onIntent: (SearchIntents) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .padding(8.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.End

    ) {
        Button(
            modifier = Modifier,
            onClick = {
                onIntent(SearchIntents.ResetFilters)
            }
        ) {
            Text(stringResource(R.string.reset_filters))
            Spacer(modifier = Modifier.size(4.dp))
            Icon(
                Icons.Default.FilterAltOff,
                null
            )
        }

        ShortInputCard(
            R.string.experienceInYears,
            R.string.enter_expirence,
            searchState.filterData.experienceInYears,
            { newValue ->
                onIntent(SearchIntents.FilterAction(FilterIntent.UpdateYears(newValue)))
            },
            keyboardType = KeyboardType.Number
        )
        ShortInputCard(
            R.string.detail_descriptions,
            R.string.enter_words,
            searchState.filterData.detailDescriptions,
            { newValue ->
                onIntent(SearchIntents.FilterAction(FilterIntent.UpdateDetailDescriptions(newValue)))
            }
        )

        ShortWithComposableCard(
            R.string.city,
            {
                EnumDropDown(
                    R.string.choice_city,
                    searchState.filterData.city,
                    CitiesEnum.entries,
                    onSelect = { city ->
                        onIntent(SearchIntents.FilterAction(FilterIntent.UpdateCity(city)))
                    }
                )
            }
        )

        ShortWithComposableCard(
            R.string.masterSpecialization,
            {
                EnumDropDown(
                    R.string.choice_spec,
                    searchState.filterData.masterSpecialization,
                    MasterSpetializationsEnum.entries,
                    onSelect = { spec ->
                        onIntent(SearchIntents.FilterAction(FilterIntent.UpdateMasterSpecialization(spec)))
                    }
                )
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onIntent(SearchIntents.ApplyFilters)
            }
        ) {
            Text(stringResource(R.string.apply_filters))
        }






    }


}


