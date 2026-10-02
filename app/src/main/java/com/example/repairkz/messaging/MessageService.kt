package com.example.repairkz.messaging

import com.example.repairkz.data.remote.api.FcmApi
import com.example.repairkz.data.remote.dto.FCM.RegisterTokenRequestDTO
import com.example.repairkz.data.userData.UserRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationHelper: NotificationHelper
    @Inject
    lateinit var fcmApi: FcmApi
    @Inject
    lateinit var userRepository: UserRepository

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.data["title"]
        val body = message.data["body"]
        notificationHelper.showNotification(title, body, message.data)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            val user = userRepository.userData.firstOrNull() ?: return@launch
            try {
                fcmApi.registerToken(RegisterTokenRequestDTO(userId = user.id, token = token))
            } catch (e: Exception) {

            }
        }
    }
}