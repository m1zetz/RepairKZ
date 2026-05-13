package com.example.repairkz.ui.features.masterInfo

import com.example.repairkz.common.models.Master
import com.example.repairkz.data.remote.dto.MasterServiceDTO
import com.example.repairkz.ui.features.UserInfo.UserIntent.MasterProfileIntent

data class MasterProfileState(
    val master: Master? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class MasterInfoIntent{
    data class DoOrder(val masterId: Long) : MasterInfoIntent()
    data class AddToFavorites(val masterId: Long) : MasterInfoIntent()
    data class Report(val masterId: Long) : MasterInfoIntent()
}