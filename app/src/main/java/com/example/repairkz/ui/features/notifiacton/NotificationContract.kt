package com.example.repairkz.ui.features.notifiacton

import com.example.repairkz.common.models.Order


data class NotificationState(
    val isLoading: Boolean = true,
    val notifications: List<Order> = emptyList(),
    val selectedOrder: Order? = null,
    val error: String? = null
)

sealed class NotificationIntent{
    data class ShowDetails(val order: Order) : NotificationIntent()
    object HideDetails : NotificationIntent()
    object GetNotifications : NotificationIntent()
}
