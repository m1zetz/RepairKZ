package com.example.RepairKZ_Backend.controller

import com.example.RepairKZ_Backend.entity.FirebaseToken
import com.example.RepairKZ_Backend.repository.FirebaseTokenRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/fcm")
class FirebaseTokenController(
    private val firebaseTokenRepository: FirebaseTokenRepository
) {

    @PostMapping("/register")
    fun registerToken(
        @RequestBody request: RegisterTokenRequest
    ): ResponseEntity<Void> {
        firebaseTokenRepository.save(FirebaseToken(userId = request.userId, token = request.token))
        return ResponseEntity.ok().build()
    }
}

data class RegisterTokenRequest(val userId: Long, val token: String)