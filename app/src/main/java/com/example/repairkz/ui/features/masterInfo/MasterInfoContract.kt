package com.example.repairkz.ui.features.masterInfo

import com.example.repairkz.common.models.Master
import com.example.repairkz.ui.base.UiEffect
import com.example.repairkz.ui.base.UiIntent
import com.example.repairkz.ui.base.UiState

data class MasterProfileState (
    val master: Master? = null,
    val isLoading: Boolean = false,
    val error: String? = null
): UiState

sealed class MasterProfileIntent : UiIntent{
    object DoOrder : MasterProfileIntent()
    data class AddToFavorites(val masterId: Long) : MasterProfileIntent()
    data class Report(val masterId: Long) : MasterProfileIntent()
}
sealed class MasterProfileEffect : UiEffect{
    data class NavigateToOrderReg(val masterId: Long) : MasterProfileEffect()
}