package com.example.RepairKZ_Backend.service

import com.example.RepairKZ_Backend.entity.User
import com.example.RepairKZ_Backend.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UserService(
    private val userRepository: UserRepository,
    private val fileService: FileService
) {

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    @Transactional
    fun createUser(user: User, file: MultipartFile?): User {
        if (userRepository.findByEmail(user.email) != null) {
            throw IllegalArgumentException("User with email ${user.email} already exists")
        }

        val url = fileService.updateUserPhoto(file)

        return userRepository.save(
            user.copy(
                userPhotoUrl = url?:"",
            )
        )
    }

    @Transactional
    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

}