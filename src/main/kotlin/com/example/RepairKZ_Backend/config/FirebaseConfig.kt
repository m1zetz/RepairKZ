package com.example.RepairKZ_Backend.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource

@Configuration
class FirebaseConfig {

    @PostConstruct
    fun initialize() {
        val serviceAccountJson = System.getenv("FIREBASE_SERVICE_ACCOUNT")
            ?: throw IllegalStateException("FIREBASE_SERVICE_ACCOUNT env var not set")

        val serviceAccount = serviceAccountJson.byteInputStream()

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }
    }
}