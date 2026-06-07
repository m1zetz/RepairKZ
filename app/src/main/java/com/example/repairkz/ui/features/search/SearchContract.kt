package com.example.repairkz.ui.features.search

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.models.Master
import com.example.repairkz.ui.base.UiEffect
import com.example.repairkz.ui.base.UiIntent
import com.example.repairkz.ui.base.UiState

data class SearchState(
    val query: String = "",
    val initialPatternResId: Int?,
    val result: SearchResult = SearchResult.Idle,
    val filterData: FilterData = FilterData(),
    val isFiltersSheetOpen: Boolean = false,
    val isFilterActive: Boolean = false,
    val searchFieldState: Boolean = true,
    val error: String = ""

) : UiState

sealed class SearchResult {
    data object Idle : SearchResult()
    data object Loading : SearchResult()
    data class Success(val masters: List<Master>) : SearchResult()
    data class Error(val message: String) : SearchResult()
}

data class FilterData(
    val experienceInYears: String = "",
    val city: CitiesEnum? = null,
    val masterSpecialization: MasterSpetializationsEnum? = null,
    val detailDescriptions: String = ""
)

sealed class SearchIntent : UiIntent {
    data class ChangeSearchFieldState(val state: Boolean) : SearchIntent()
    data class ChangeText(val text: String) : SearchIntent()
    object GetData : SearchIntent()
    object NavigateToBack : SearchIntent()
    data class NavigateToUserInfo(val id: Long) : SearchIntent()

    object OpenFilters : SearchIntent()
    object CloseFilters : SearchIntent()

    object ApplyFilters : SearchIntent()
    object ResetFilters : SearchIntent()
    data class FilterAction(val action: FilterIntent) : SearchIntent()


}

sealed class FilterIntent{
    data class UpdateMasterSpecialization(val spec: MasterSpetializationsEnum) : FilterIntent()
    data class UpdateYears(val years: String) : FilterIntent()
    data class UpdateDetailDescriptions(val words: String) : FilterIntent()
    data class UpdateCity(val city: CitiesEnum) : FilterIntent()

}
sealed interface SearchEffect : UiEffect {
    object NavigateBack : SearchEffect

    data class NavigateToMasterInfo(val id: Long) : SearchEffect
}