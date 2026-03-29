package com.example.RepairKZ_Backend.controller

import com.example.RepairKZ_Backend.model.RegistrationResponseDTO
import com.example.RepairKZ_Backend.model.LoginRequestDTO
import com.example.RepairKZ_Backend.model.LoginResponseDTO
import com.example.RepairKZ_Backend.model.RefreshResponseDTO
import com.example.RepairKZ_Backend.model.UserRegistrationDTO
import com.example.RepairKZ_Backend.service.AuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/register")
    fun register(
        @RequestBody user: UserRegistrationDTO
    ): RegistrationResponseDTO {
        return authService.register(user)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequest: LoginRequestDTO
    ): LoginResponseDTO {
        return authService.login(loginRequest)
    }

    @GetMapping("/refresh-token")
    fun refreshToken() : RefreshResponseDTO {
        return authService.refreshToken()
    }
}