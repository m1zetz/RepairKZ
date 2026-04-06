package com.example.repairkz.ui.features.notifiacton

import com.example.repairkz.common.enums.OrderRequestStatus
import com.example.repairkz.common.enums.OrderStatus
import com.example.repairkz.common.models.Order
import com.example.repairkz.data.remote.dto.order.ClientHistoryItemDTO
import com.example.repairkz.data.remote.dto.order.MasterHistoryItemDTO
import java.time.LocalDateTime

sealed class HistoryItem {
    abstract val createdAt: String?

    data class MasterItem(val data: MasterHistoryItemDTO) : HistoryItem(){
        override val createdAt = data.createdAt
    }
    data class ClientItem(val data: ClientHistoryItemDTO) : HistoryItem(){
        override val createdAt = data.createdAt
    }
}

sealed class NotificationState {
    object Loading : NotificationState()
    data class Success(
        val notifications: List<HistoryItem>,
        val selectedOrder: HistoryItem? = null
    ) : NotificationState()
    data class Error(val message: String) : NotificationState()
}

sealed class NotificationIntent {
    data class ShowDetails(val order: HistoryItem?) : NotificationIntent()
    object HideDetails : NotificationIntent()
    object GetNotifications : NotificationIntent()
    data class AcceptOrReject(val status: OrderRequestStatus, val request: HistoryItem.MasterItem) : NotificationIntent()
    data class ChangeOrderStatus(val status: OrderStatus, val order: HistoryItem.ClientItem) : NotificationIntent()
}
