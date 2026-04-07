package com.example.RepairKZ_Backend.service

import com.example.RepairKZ_Backend.common.enums.StatusOfUser
import com.example.RepairKZ_Backend.common.extensions.toResponseDTO
import com.example.RepairKZ_Backend.entity.EmailVerification
import com.example.RepairKZ_Backend.entity.Master
import com.example.RepairKZ_Backend.entity.User
import com.example.RepairKZ_Backend.model.ChangeStatusRequestDTO
import com.example.RepairKZ_Backend.model.EmailDTO
import com.example.RepairKZ_Backend.model.MasterInfoDTO
import com.example.RepairKZ_Backend.model.MasterRequestDTO
import com.example.RepairKZ_Backend.model.MasterResponseDTO
import com.example.RepairKZ_Backend.model.UserRegistrationDTO
import com.example.RepairKZ_Backend.model.UserResponseDTO
import com.example.RepairKZ_Backend.repository.EmailVerificationRepository
import com.example.RepairKZ_Backend.repository.MasterRepository
import com.example.RepairKZ_Backend.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import kotlin.collections.map
import kotlin.random.Random


@Service
class UserService(
    private val userRepository: UserRepository,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val fileService: FileService,
    private val mailSenderService: MailSenderService,
    private val passwordEncoder: PasswordEncoder,
    private val masterRepository: MasterRepository,
) {

    fun getMasters(currentId: Long) : List<MasterInfoDTO> {
        return masterRepository.findAllWithoutId(currentId).map { master ->
                master.toInfoDto()
        }
    }

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
    fun getMasterById(id: Long): MasterResponseDTO? {
        return masterRepository.findByUserId(id)?.let { master ->
            MasterResponseDTO(
                master.user?.toResponseDTO() ?: throw IllegalStateException("Master without user entity"),
                master.experienceInYears,
                master.description,
                master.masterSpecialization,
            )
        }
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
    fun updateUser(generalUser: MasterResponseDTO) : MasterResponseDTO {
        val user = userRepository.findByIdOrNull(generalUser.user.id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        user.apply {
            userPhotoUrl = generalUser.user.userPhotoUrl
            firstName = generalUser.user.firstName
            lastName = generalUser.user.lastName
            email = generalUser.user.email
            city = generalUser.user.city
            phoneNumber = generalUser.user.phone
        }
        val savedMaster = saveMaster(
            user = user,
            generalUser
        )
        return MasterResponseDTO(
            user = user.toResponseDTO(),
            experienceInYears = savedMaster.experienceInYears,
            description = savedMaster.description,
            masterSpecialization = savedMaster.masterSpecialization
        )
    }

    private fun saveMaster(user: User, inputMaster: MasterResponseDTO): Master {
        val master = masterRepository.findByUserId(user.id!!) ?: Master(user = user)
        master.apply {
            experienceInYears = inputMaster.experienceInYears ?: experienceInYears
            description = inputMaster.description ?: description
            masterSpecialization = inputMaster.masterSpecialization ?: masterSpecialization
        }
        return masterRepository.save(master)
    }

    @Transactional
    fun updateUserPhoto(id: Long, file: MultipartFile) : String {
        val user = userRepository.findByIdOrNull(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val url = fileService.savePhotoAndGetUrl(file) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST)
        user.userPhotoUrl = url
        userRepository.save(user)
        return url
    }

    @Transactional
    fun changeStatus(id: Long, dto: ChangeStatusRequestDTO): MasterResponseDTO {
        val user = userRepository.findByIdOrNull(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User not found"
        )
        user.status = dto.statusOfUser
        if (dto.statusOfUser == StatusOfUser.CLIENT) {
            return MasterResponseDTO(user.toResponseDTO())
        }
        val masterDataDto = MasterResponseDTO(
            user.toResponseDTO(),
            experienceInYears =  dto.masterData?.experienceInYears,
            description = dto.masterData?.description,
            masterSpecialization = dto.masterData?.masterSpecialization
        )
        val savedMaster = saveMaster(user, masterDataDto)
        return MasterResponseDTO(
            user = user.toResponseDTO(),
            experienceInYears = savedMaster.experienceInYears,
            description = savedMaster.description,
            masterSpecialization = savedMaster.masterSpecialization
        )
    }

    fun findById(id: Long): User? {
        return userRepository.findByIdOrNull(id)
    }

    @Transactional
    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

}

