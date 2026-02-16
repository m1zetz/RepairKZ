package com.example.repairkz.ui.features.search

import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.models.Master
import com.example.repairkz.ui.features.UserInfo.UserTypes

data class SearchUiState(
    val query: String = "",
    val initialPatternResId: Int?,
    val result: SearchResult = SearchResult.Idle,
    val filterData: FilterData = FilterData(),
    val isFiltersSheetOpen: Boolean = false,
    val isFilterActive: Boolean = false
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
    data class ChangeText(val text: String) : SearchIntents()
    object GetData : SearchIntents()
    object NavigateToBack : SearchIntents()
    data class NavigateToUserInfo(val id: Int) : SearchIntents()

    object OpenFilters : SearchIntents()
    object CloseFilters : SearchIntents()

    object ApplyFilters : SearchIntents()
    object ResetFilters : SearchIntents()
    data class FilterActions(val action: FilterIntents) : SearchIntents()


}

sealed class FilterIntents{
    data class UpdateMasterSpecialization(val spec: MasterSpetializationsEnum) : FilterIntents()
    data class UpdateYears(val years: String) : FilterIntents()
    data class UpdateDetailDescriptions(val words: String) : FilterIntents()
    data class UpdateCity(val city: CitiesEnum) : FilterIntents()

}
sealed interface SearchEffects {
    object NavigateBack : SearchEffects

    data class NavigateToUserInfo(val id: Int) : SearchEffects
}