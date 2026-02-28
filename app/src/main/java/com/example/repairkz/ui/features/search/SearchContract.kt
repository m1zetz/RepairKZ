package com.example.repairkz.ui.features.search

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.models.Master

data class SearchUiState(
    val query: String = "",
    val initialPatternResId: Int?,
    val result: SearchResult = SearchResult.Idle,
    val filterData: FilterData = FilterData(),
    val isFiltersSheetOpen: Boolean = false,
    val isFilterActive: Boolean = false,
    val searchFieldState: Boolean = true

)

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

sealed class SearchIntents {
    data class ChangeSearchFieldState(val state: Boolean) : SearchIntents()
    data class ChangeText(val text: String) : SearchIntents()
    object GetData : SearchIntents()
    object NavigateToBack : SearchIntents()
    data class NavigateToUserInfo(val id: Int) : SearchIntents()

    object OpenFilters : SearchIntents()
    object CloseFilters : SearchIntents()

    object ApplyFilters : SearchIntents()
    object ResetFilters : SearchIntents()
    data class FilterAction(val action: FilterIntent) : SearchIntents()


}

sealed class FilterIntent{
    data class UpdateMasterSpecialization(val spec: MasterSpetializationsEnum) : FilterIntent()
    data class UpdateYears(val years: String) : FilterIntent()
    data class UpdateDetailDescriptions(val words: String) : FilterIntent()
    data class UpdateCity(val city: CitiesEnum) : FilterIntent()

}
sealed interface SearchEffects {
    object NavigateBack : SearchEffects

    data class NavigateToUserInfo(val id: Int) : SearchEffects
}