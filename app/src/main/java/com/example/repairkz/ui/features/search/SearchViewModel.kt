package com.example.repairkz.ui.features.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.domain.useCases.searchData.GetMastersUseCase
import com.example.repairkz.ui.features.search.SearchEffects.*
import com.example.repairkz.ui.features.search.SearchResult.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMastersUseCase: GetMastersUseCase
) : ViewModel() {

    private val initialPatternResId: Int? = savedStateHandle.get<Int>("pattern")
    private val _uiState = MutableStateFlow(SearchUiState(initialPatternResId = initialPatternResId))
    val uiState = _uiState.asStateFlow()

    private val _searchEffectsChannel = Channel<SearchEffects>(Channel.BUFFERED)
    val searchEffectsChannel = _searchEffectsChannel.receiveAsFlow()

    fun handleIntent(intent: SearchIntents) {
        when (intent) {
            is SearchIntents.ChangeText -> _uiState.update {
                it.copy(intent.text, initialPatternResId = null)
            }
            is SearchIntents.NavigateToBack -> {
                viewModelScope.launch {
                    _searchEffectsChannel.send(NavigateBack)
                }
            }
            is SearchIntents.GetData -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(result = Loading)
                    }
                    try{
                        _uiState.update {
                            it.copy(result = Success(getMastersUseCase()))
                        }
                    } catch (e: Exception){
                        _uiState.update {
                            it.copy(result = Error("Ошибка запроса"))
                        }
                    }
                }
            }

            is SearchIntents.NavigateToUserInfo -> {
                viewModelScope.launch {
                    _searchEffectsChannel.send(NavigateToUserInfo(intent.id))
                }
            }


            SearchIntents.OpenFilters -> {
                _uiState.update {
                    it.copy(isFiltersSheetOpen = true)
                }
            }
            SearchIntents.CloseFilters -> {
                _uiState.update {
                    it.copy(isFiltersSheetOpen = false)
                }
            }

            is SearchIntents.FilterActions -> {
                handleFilterAction(intent.action)
            }


            SearchIntents.ApplyFilters -> {

            }
            SearchIntents.ResetFilters -> {
                _uiState.update {
                    it.copy(filterData = FilterData())
                }
            }
        }
    }
    fun handleFilterAction(action: FilterIntents){
        when(action){
            is FilterIntents.UpdateCity -> {
                _uiState.update {
                    it.copy(
                        filterData = it.filterData.copy(
                            city = action.city
                        )
                    )
                }
            }
            is FilterIntents.UpdateDetailDescriptions -> {
                _uiState.update {
                    it.copy(
                        filterData = it.filterData.copy(
                            detailDescriptions = action.words
                        )
                    )
                }
            }
            is FilterIntents.UpdateMasterSpecialization -> {
                _uiState.update {
                    it.copy(
                        filterData = it.filterData.copy(
                            masterSpecialization = action.spec
                        )
                    )
                }
            }
            is FilterIntents.UpdateYears -> {
                _uiState.update {
                    it.copy(
                        filterData = it.filterData.copy(
                            experienceInYears = action.years
                        )
                    )
                }
            }
        }
    }
}


