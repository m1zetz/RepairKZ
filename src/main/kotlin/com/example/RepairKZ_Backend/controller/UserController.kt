package com.example.RepairKZ_Backend.controller


import com.example.RepairKZ_Backend.model.CodeCheckDTO
import com.example.RepairKZ_Backend.model.EmailRequestDTO
import com.example.RepairKZ_Backend.model.UserRegistrationDTO
import com.example.RepairKZ_Backend.model.UserResponseDTO
import com.example.RepairKZ_Backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping
    fun getALlUsers(): List<UserResponseDTO> {
        return userService.getAllUsers()
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): UserResponseDTO? {
        return userService.getUserById(id)
    }



    @PostMapping
    fun createUser(
        @RequestPart("user") user: UserRegistrationDTO,
        @RequestPart("file") file: MultipartFile?
    ): UserResponseDTO {

        return userService.createUser(user, file)
    }

    @PostMapping("/get-code")
    fun emailResieve(
        @RequestBody requestDTO: EmailRequestDTO,
    ) {
        return userService.emailVerification(requestDTO.email)
    }

    @PostMapping("/check-code")
    fun codeResieve(
        @RequestBody checkDTO: CodeCheckDTO
    ): ResponseEntity<Unit> {
        return userService.codeRecieve(checkDTO.email, checkDTO.code).fold(
            onSuccess = { ResponseEntity.ok().build() },
            onFailure = { ResponseEntity.badRequest().build() }
        )
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }

}