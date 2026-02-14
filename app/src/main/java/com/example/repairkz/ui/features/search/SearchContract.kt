package com.example.repairkz.ui.features.search

import com.example.repairkz.common.enums.Cities
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.models.Master

data class SearchUiState(
    val query: String = "",
    val initialPatternResId: Int?,
    val result: SearchResult = SearchResult.Idle,
    val filterData: FilterData = FilterData(),
    val isFiltersSheetOpen: Boolean = false

)

sealed class SearchResult {
    data object Idle : SearchResult()
    data object Loading : SearchResult()
    data class Success(val masters: List<Master>) : SearchResult()
    data class Error(val message: String) : SearchResult()
}

data class FilterData(
    val experienceInYears: Int? = null,
    val city: Cities? = null,
    val masterSpecialization: MasterSpetializationsEnum? = null,
    val detailDescriptions: String? = null
)

sealed class SearchIntents {
    data class ChangeText(val text: String) : SearchIntents()
    object GetData : SearchIntents()
    object NavigateToBack : SearchIntents()
    data class NavigateToUserInfo(val id: Int) : SearchIntents()
    object OpenFilters : SearchIntents()
    object CloseFilters : SearchIntents()

    data class FilterActions(val action: FilterIntents) : SearchIntents()
}

sealed class FilterIntents{
    data class UpdateMasterSpecialization(val spec: MasterSpetializationsEnum) : FilterIntents()
    data class UpdateYears(val years: Int) : FilterIntents()
    data class UpdateDetailDescriptions(val words: String) : FilterIntents()
    data class UpdateCity(val city: Cities) : FilterIntents()

}
sealed interface SearchEffects {
    object NavigateBack : SearchEffects

    data class NavigateToUserInfo(val id: Int) : SearchEffects
}