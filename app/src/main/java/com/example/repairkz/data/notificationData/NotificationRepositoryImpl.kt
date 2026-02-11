package com.example.repairkz.data.notificationData

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.repairkz.common.enums.PaymentMethod
import com.example.repairkz.common.models.Order

import jakarta.inject.Inject
import kotlinx.coroutines.delay
import java.math.BigDecimal
import java.time.LocalDateTime

class NotificationRepositoryImpl @Inject constructor() : NotificationRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getNotifications(): List<Order> {
        delay(3000)
        val order = Order(
            "Сантехник",
            "Антон",
            LocalDateTime.of(2021, 3, 27, 2, 16, 20),
            "Починить трубопровод",
            BigDecimal("5600.00"),
            PaymentMethod.CASH
        )
        return listOf(order)
    }
}