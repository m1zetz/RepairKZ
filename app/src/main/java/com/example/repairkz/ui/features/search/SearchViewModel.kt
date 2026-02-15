package com.example.repairkz.ui.features.search

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.models.Master
import com.example.repairkz.domain.useCases.masterData.GetMastersUseCase
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
                        val result = getMastersUseCase()

                        val filter = _uiState.value.filterData
                        result.onSuccess { masters ->

                            val mastersFromSearch = masters.filter { master ->

                                val name = _uiState.value.query.isEmpty() || master.masterName.uppercase().contains(_uiState.value.query.uppercase())
                                name
                            }
                            if(_uiState.value.isFilterActive){
                                val sorteredMasters = masters.filter { master ->

                                    val city = filter.city == null || master.city == filter.city
                                    val spec = filter.masterSpecialization == null || master.masterSpecialization == filter.masterSpecialization
                                    val descriptions = filter.detailDescriptions.isEmpty() || master.description.contains(filter.detailDescriptions)
                                    val years = filter.experienceInYears.isEmpty() || master.experienceInYears >= (filter.experienceInYears.toIntOrNull()?:0)
                                    city && spec && descriptions && years
                                }
                                _uiState.update {
                                        it.copy(result = Success(sorteredMasters))
                                    }
                            } else{
                                _uiState.update {
                                    it.copy(result = Success(mastersFromSearch))
                                }
                            }
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
                _uiState.update {
                    it.copy(isFilterActive = true, isFiltersSheetOpen = false)
                }
                handleIntent(SearchIntents.GetData)
            }

            SearchIntents.ResetFilters -> {
                _uiState.update {
                    it.copy(filterData = FilterData(), isFilterActive = false)
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


