package com.example.repairkz.ui.features.notifiacton

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.repairkz.common.enums.OrderRequestStatus
import com.example.repairkz.common.enums.OrderStatus
import com.example.repairkz.common.models.Order
import com.example.repairkz.common.models.User
import com.example.repairkz.data.remote.dto.order.ClientHistoryItemDTO
import com.example.repairkz.data.remote.dto.order.MasterHistoryItemDTO
import java.time.LocalDateTime

sealed class HistoryItem {
    abstract val createdAt: LocalDateTime?

    data class MasterItem(val data: MasterHistoryItemDTO) : HistoryItem() {
        @RequiresApi(Build.VERSION_CODES.O)
        override val createdAt = data.createdAt
            ?.takeIf { it != "null" && it.isNotBlank() }
            ?.let { LocalDateTime.parse(it) }
    }

    data class ClientItem(val data: ClientHistoryItemDTO) : HistoryItem() {
        @RequiresApi(Build.VERSION_CODES.O)
        override val createdAt = data.createdAt
            ?.takeIf { it != "null" && it.isNotBlank() }
            ?.let { LocalDateTime.parse(it) }
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
