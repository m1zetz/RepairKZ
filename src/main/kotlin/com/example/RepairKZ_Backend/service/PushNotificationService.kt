package com.example.RepairKZ_Backend.service

import com.example.RepairKZ_Backend.repository.FirebaseTokenRepository
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MessagingErrorCode
import org.springframework.stereotype.Service

@Service
class PushNotificationService(
    private val firebaseTokenRepository: FirebaseTokenRepository
) {

    fun sendToUser(userId: Long, title: String, body: String, data: Map<String, String> = emptyMap()) {
        val tokenEntity = firebaseTokenRepository.findByUserId(userId) ?: return

        val message = Message.builder()
            .setToken(tokenEntity.token)
            .putAllData(data + mapOf("title" to title, "body" to body))
            .build()

        try {
            FirebaseMessaging.getInstance().send(message)
        } catch (e: FirebaseMessagingException) {
            if (e.messagingErrorCode == MessagingErrorCode.UNREGISTERED) {
                firebaseTokenRepository.deleteByToken(tokenEntity.token)
            }
        }
    }
}