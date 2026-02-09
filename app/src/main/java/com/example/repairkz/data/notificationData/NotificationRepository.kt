package com.example.repairkz.data.notificationData

import com.example.repairkz.common.models.Order

interface NotificationRepository {
    suspend fun getNotifications(): List<Order>
}