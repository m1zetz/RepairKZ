package com.example.repairkz.ui.features.notifiacton

import com.example.repairkz.common.models.Order


//data class NotificationState(
//    val isLoading: Boolean = true,
//    val notifications: List<Order> = emptyList(),
//    val selectedOrder: Order? = null,
//    val error: String? = null
//)

sealed class NotificationState {
    object Loading : NotificationState()
    data class Success(
        val notifications: List<Order>,
        val selectedOrder: Order? = null
    ) : NotificationState()
    data class Error(val message: String) : NotificationState()
}

sealed class NotificationIntent {
    data class ShowDetails(val order: Order) : NotificationIntent()
    object HideDetails : NotificationIntent()
    object GetNotifications : NotificationIntent()
}
