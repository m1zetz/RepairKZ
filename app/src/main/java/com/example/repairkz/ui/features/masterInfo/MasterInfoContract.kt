package com.example.repairkz.ui.features.masterInfo

import com.example.repairkz.common.models.Master
import com.example.repairkz.data.remote.dto.MasterServiceDTO

data class MasterProfileState(
    val master: Master? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class MasterInfoIntent{
    object DoOrder : MasterInfoIntent()
    data class AddToFavorites(val masterId: Long) : MasterInfoIntent()
    data class Report(val masterId: Long) : MasterInfoIntent()
}
sealed class MasterInfoEffect{
    data class NavigateToOrderReg(val masterId: Long) : MasterInfoEffect()
}