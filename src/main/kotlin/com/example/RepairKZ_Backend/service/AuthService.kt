package com.example.RepairKZ_Backend.service

import com.example.RepairKZ_Backend.common.enums.StatusOfUser
import com.example.RepairKZ_Backend.common.extensions.toResponseDTO
import com.example.RepairKZ_Backend.entity.Master
import com.example.RepairKZ_Backend.entity.User
import com.example.RepairKZ_Backend.model.RegistrationResponseDTO
import com.example.RepairKZ_Backend.model.LoginRequestDTO
import com.example.RepairKZ_Backend.model.LoginResponseDTO
import com.example.RepairKZ_Backend.model.MasterResponseDTO
import com.example.RepairKZ_Backend.model.MasterShortInfoDTO
import com.example.RepairKZ_Backend.model.RefreshResponseDTO
import com.example.RepairKZ_Backend.model.UserRegistrationDTO
import com.example.RepairKZ_Backend.repository.MasterRepository
import com.example.RepairKZ_Backend.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val master: MasterRepository,
    private val fileService: FileService,
    private val passwordEncoder: PasswordEncoder,
    private val tokenService: TokenService
) {

    @Transactional
    fun login(
        loginRequest: LoginRequestDTO,
    ): LoginResponseDTO {
        val user = userRepository.findByEmail(loginRequest.login) ?: throw IllegalArgumentException("User not found")
        val masterData = master.findByUser(user)
        if (!passwordEncoder.matches(loginRequest.password, user.password)) {
            throw IllegalArgumentException("Invalid password")
        }
        val token = tokenService.createToken(user)
        val baseResponse = LoginResponseDTO(
            id = user.id!!,
            token = token,
            user = user.toResponseDTO()
        )
        return when (user.status) {
            StatusOfUser.CLIENT -> {
                baseResponse
            }

            StatusOfUser.MASTER -> {
                baseResponse.copy(
                    master = MasterShortInfoDTO(
                        masterData?.experienceInYears,
                        masterData?.description,
                        masterData?.masterSpecialization
                    )
                )
            }
        }


    }

    @Transactional
    fun register(user: UserRegistrationDTO): RegistrationResponseDTO {
        if (userRepository.findByEmail(user.email) != null) {
            throw IllegalArgumentException("User with email ${user.email} already exists")
        }
        val hashPassword = passwordEncoder.encode(user.password)
        val newUser = User(
            firstName = user.firstName,
            email = user.email,
            phoneNumber = user.phone,
            lastName = user.lastName,
            city = user.city,
            id = null,
            userPhotoUrl = null,
            status = StatusOfUser.CLIENT,
            password = hashPassword!!
        )
        val savedUser = userRepository.save(
            newUser
        )
        println("Saved user id: ${savedUser.id}")
        return RegistrationResponseDTO(
            id = savedUser.id!!,
            token = tokenService.createToken(savedUser),
        )

    }
    fun refreshToken(): RefreshResponseDTO {
        val user = SecurityContextHolder.getContext().authentication?.principal as User
        val newToken = tokenService.createToken(user)
        return RefreshResponseDTO(
            token = newToken,
        )
    }

}