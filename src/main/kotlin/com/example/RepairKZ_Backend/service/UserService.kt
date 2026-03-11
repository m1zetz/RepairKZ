package com.example.RepairKZ_Backend.service

import com.example.RepairKZ_Backend.common.extensions.toResponseDTO
import com.example.RepairKZ_Backend.entity.EmailVerification
import com.example.RepairKZ_Backend.entity.User
import com.example.RepairKZ_Backend.model.EmailDTO
import com.example.RepairKZ_Backend.model.UserRegistrationDTO
import com.example.RepairKZ_Backend.model.UserResponseDTO
import com.example.RepairKZ_Backend.repository.EmailVerificationRepository
import com.example.RepairKZ_Backend.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import kotlin.collections.map
import kotlin.random.Random


@Service
class UserService(
    private val userRepository: UserRepository,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val fileService: FileService,
    private val mailSenderService: MailSenderService,
    private val passwordEncoder: PasswordEncoder
) {

    fun getAllUsers(): List<UserResponseDTO> {
        return userRepository.findAll().map {
            it.toResponseDTO()
        }
    }

    fun getUserById(id: Long): UserResponseDTO? {
        return userRepository.findById(id).map {
            it.toResponseDTO()
        }.orElse(null)
    }


    @Transactional
    fun emailVerification(email: String) {
        val code = Random.nextInt(100000, 999999)
        emailVerificationRepository.deleteByEmail(email)
        emailVerificationRepository.save(EmailVerification(email = email, code = code))
        mailSenderService.sendMail(
            EmailDTO(
                email,
                "Hello, please verify your email for RepairKZ",
                text = "Code $code",
            )
        )
    }

    @Transactional
    fun codeRecieve(email: String, code: Int): Result<String> {

        if (emailVerificationRepository.existsByEmailAndCode(email, code)) {
            emailVerificationRepository.deleteByEmail(email)
            return Result.success("success")
        }
        return Result.failure(Exception("Wrong code"))
    }

    @Transactional
    fun createUser(user: UserRegistrationDTO, file: MultipartFile?): UserResponseDTO {
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
            status = user.status,
            password = hashPassword!!
        )

        val url = fileService.updateUserPhoto(file)

        return userRepository.save(
            newUser.copy(
                userPhotoUrl = url ?: ""
            )
        ).toResponseDTO()



    }

    @Transactional
    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

}

