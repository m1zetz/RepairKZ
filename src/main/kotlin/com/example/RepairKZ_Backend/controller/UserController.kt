package com.example.RepairKZ_Backend.controller


import com.example.RepairKZ_Backend.common.enums.MasterSpetializationsEnum
import com.example.RepairKZ_Backend.common.enums.StatusOfUser
import com.example.RepairKZ_Backend.model.ChangeStatusRequestDTO
import com.example.RepairKZ_Backend.model.CodeCheckDTO
import com.example.RepairKZ_Backend.model.EmailRequestDTO
import com.example.RepairKZ_Backend.model.MasterInfoDTO
import com.example.RepairKZ_Backend.model.MasterResponseDTO
import com.example.RepairKZ_Backend.model.UpdatePhotoResponseDTO
import com.example.RepairKZ_Backend.model.UserRegistrationDTO
import com.example.RepairKZ_Backend.model.UserResponseDTO
import com.example.RepairKZ_Backend.service.MasterService
import com.example.RepairKZ_Backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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

    @PutMapping("/update-photo/{id}")
    fun updateUserPhotoById(
        @PathVariable("id") id: Long,
        @RequestPart file: MultipartFile
    ): ResponseEntity<UpdatePhotoResponseDTO>{
        val result = userService.updateUserPhoto(id, file)
        return ResponseEntity.ok(UpdatePhotoResponseDTO(result))
    }

    @PutMapping("/update-user")
    fun updateUser(
        @RequestBody master: MasterResponseDTO,
    ): ResponseEntity<MasterResponseDTO> {
        val result = userService.updateUser(master)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/get-masters/{currentId}")
    fun getMasters(@PathVariable currentId: Long): ResponseEntity<List<MasterInfoDTO>> {
        val response = userService.getMasters(currentId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/get-master-by-id/{id}")
    fun getMasterById(@PathVariable id: Long): ResponseEntity<MasterResponseDTO> {
        val master = userService.getMasterById(id)
        return ResponseEntity.ok(master)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): UserResponseDTO? {
        return userService.getUserById(id)
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

    @PutMapping("/change-status/{id}")
    fun changeStatus(
        @PathVariable id: Long,
        @RequestBody masterData: ChangeStatusRequestDTO
    ): ResponseEntity<MasterResponseDTO> {
        val result = userService.changeStatus(id, masterData)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }

}