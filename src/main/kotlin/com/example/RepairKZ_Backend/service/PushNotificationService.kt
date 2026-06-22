package com.example.RepairKZ_Backend.service

import com.example.RepairKZ_Backend.repository.FirebaseTokenRepository
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MessagingErrorCode
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PushNotificationService(
    private val firebaseTokenRepository: FirebaseTokenRepository
) {
    private val logger = LoggerFactory.getLogger(PushNotificationService::class.java)

    fun sendToUser(userId: Long, title: String, body: String, data: Map<String, String> = emptyMap()) {
        val tokenEntity = firebaseTokenRepository.findByUserId(userId)
        if (tokenEntity == null) {
            logger.warn("FCM token not found for userId: $userId")
            return
        }
        logger.info("Sending FCM to userId: $userId, token: ${tokenEntity.token}")

        val message = Message.builder()
            .setToken(tokenEntity.token)
            .putAllData(data + mapOf("title" to title, "body" to body))
            .build()

        try {
            val result = FirebaseMessaging.getInstance().send(message)
            logger.info("FCM sent successfully: $result")
        } catch (e: FirebaseMessagingException) {
            logger.error("FCM error: ${e.message}, code: ${e.messagingErrorCode}")
            if (e.messagingErrorCode == MessagingErrorCode.UNREGISTERED) {
                firebaseTokenRepository.deleteByToken(tokenEntity.token)
            }
        }
    }
}