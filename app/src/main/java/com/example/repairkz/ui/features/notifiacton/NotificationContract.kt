package com.example.repairkz.ui.features.notifiacton

import android.os.Message
import com.example.repairkz.common.models.Order

sealed class NotificationState {
    object isLoading : NotificationState()
    data class Success(val notifications: List<Order>) : NotificationState()
    data class Error(val message: String) : NotificationState()
}