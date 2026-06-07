package com.example.repairkz.ui.features.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.domain.useCases.masterData.GetMastersUseCase
import com.example.repairkz.ui.base.BaseViewModel
import com.example.repairkz.ui.features.search.SearchEffect.*
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
    private val getMastersUseCase: GetMastersUseCase,
) : BaseViewModel<SearchState, SearchIntent, SearchEffect>() {

    private val comingId: Int? = savedStateHandle.get<Int>("pattern")

    override val initialState = SearchState(initialPatternResId = comingId)

    init {
        comingId?.let { id ->
            val masterSpecialization = MasterSpetializationsEnum.getSpecByResId(id)
            if (masterSpecialization != MasterSpetializationsEnum.UNKNOWN) {
                handleIntent(
                    SearchIntent.FilterAction(
                        FilterIntent.UpdateMasterSpecialization(
                            masterSpecialization
                        )
                    )
                )
                handleIntent(SearchIntent.ApplyFilters)
            }
        }

    }

    override fun handleIntent(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.ChangeText -> {
                setState {
                    copy(intent.text, initialPatternResId = null)
                }
            }

            is SearchIntent.NavigateToBack -> {
                sendEffect(NavigateBack)
            }

            is SearchIntent.GetData -> {
                viewModelScope.launch {
                    setState {
                        copy(result = Loading)
                    }
                    try {
                        val result = getMastersUseCase()

                        val filter = _state.value.filterData
                        result.onSuccess { masters ->

                            val mastersFromSearch = masters.filter { master ->

                                val name =
                                    _state.value.query.isEmpty() || master.firstName.uppercase()
                                        .contains(_state.value.query.uppercase())
                                name
                            }
                            if (_state.value.isFilterActive) {
                                val sorteredMasters = masters.filter { master ->
                                    val city = filter.city == null || master.city == filter.city
                                    val spec =
                                        filter.masterSpecialization == null || master.masterSpecialization == filter.masterSpecialization
                                    val descriptions =
                                        filter.detailDescriptions.isEmpty() || master.description?.contains(
                                            filter.detailDescriptions
                                        ) ?: false
                                    val years =
                                        filter.experienceInYears.isEmpty() || (master.experienceInYears
                                            ?: 0) >= (filter.experienceInYears.toIntOrNull() ?: 0)
                                    city && spec && descriptions && years
                                }
                                _state.update {
                                    it.copy(result = Success(sorteredMasters))
                                }
                            } else {
                                _state.update {
                                    it.copy(result = Success(mastersFromSearch))
                                }
                            }
                        }.onFailure {
                            setState {
                                copy(
                                    result = Error(it.message ?: "")
                                )
                            }
                        }

                    } catch (e: Exception) {
                        setState {
                            copy(result = Error("Ошибка запроса"))
                        }
                    }
                }
            }

            is SearchIntent.NavigateToUserInfo -> {
                sendEffect(NavigateToMasterInfo(intent.id))
            }


            SearchIntent.OpenFilters -> {
                setState {
                    copy(isFiltersSheetOpen = true)
                }
            }

            SearchIntent.CloseFilters -> {
                setState {
                    copy(isFiltersSheetOpen = false)
                }
            }

            is SearchIntent.FilterAction -> {
                handleFilterAction(intent.action)
            }


            SearchIntent.ApplyFilters -> {
                setState {
                    copy(isFilterActive = true, isFiltersSheetOpen = false)
                }
                handleIntent(SearchIntent.GetData)
            }

            SearchIntent.ResetFilters -> {
                setState {
                    copy(filterData = FilterData(), isFilterActive = false)
                }
                handleIntent(SearchIntent.GetData)
            }

            is SearchIntent.ChangeSearchFieldState -> {
                if (!intent.state) {
                    sendEffect(NavigateBack)
                }

            }
        }
    }

    fun handleFilterAction(action: FilterIntent) {
        when (action) {
            is FilterIntent.UpdateCity -> {
                setState {
                    copy(
                        filterData = filterData.copy(
                            city = action.city
                        )
                    )
                }
            }

            is FilterIntent.UpdateDetailDescriptions -> {
                setState {
                    copy(
                        filterData = filterData.copy(
                            detailDescriptions = action.words
                        )
                    )
                }
            }

            is FilterIntent.UpdateMasterSpecialization -> {
                setState {
                    copy(
                        filterData = filterData.copy(
                            masterSpecialization = action.spec
                        )
                    )
                }
            }

            is FilterIntent.UpdateYears -> {
                setState {
                    copy(
                        filterData = filterData.copy(
                            experienceInYears = action.years
                        )
                    )
                }

            }
        }
    }
}


