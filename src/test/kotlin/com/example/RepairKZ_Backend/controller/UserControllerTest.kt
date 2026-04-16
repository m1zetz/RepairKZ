package com.example.RepairKZ_Backend.controller

import com.example.RepairKZ_Backend.common.enums.CitiesEnum
import com.example.RepairKZ_Backend.common.enums.StatusOfUser
import com.example.RepairKZ_Backend.model.MasterResponseDTO
import com.example.RepairKZ_Backend.model.UserResponseDTO
import com.example.RepairKZ_Backend.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath


@WebMvcTest(UserController::class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper
    @MockkBean
    private lateinit var userService: UserService

    @Test
    fun `should return user by id`() {
        val userId = 1L
        val userResponse = UserResponseDTO(
            id = userId,
            firstName = "Ivan",
            lastName = "Ivanov",
            email = "test@mail.com",
            phone = "87771234567",
            city = CitiesEnum.SHYMKENT,
            userPhotoUrl = null,
            status = StatusOfUser.CLIENT
        )

        every { userService.getUserById(userId) } returns userResponse

        mockMvc.perform(get("/api/users/$userId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.firstName").value("Ivan"))
            .andExpect(jsonPath("$.email").value("test@mail.com"))
    }

    @Test
    @WithMockUser
    fun `should update user profile information`() {
        val userDto = UserResponseDTO(
            id = 1L,
            firstName = "Maxim",
            lastName = "Ius",
            email = "test@repair.kz",
            phone = "87771112233",
            city = CitiesEnum.KARAGANDY,
            userPhotoUrl = "http://photo.com/1.jpg",
            status = StatusOfUser.CLIENT
        )

        val masterUpdateDto = MasterResponseDTO(
            user = userDto,
            experienceInYears = 3,
            description = "Ремонт шкавовых туалетов",
            masterSpecialization = null
        )

        every { userService.updateUser(any()) } returns masterUpdateDto

        mockMvc.perform(
            put("/api/users/update-user")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(masterUpdateDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.user.firstName").value("Maxim"))
            .andExpect(jsonPath("$.experienceInYears").value(3))
            .andExpect(jsonPath("$.description").value("Ремонт шкавовых туалетов"))

        verify(exactly = 1) { userService.updateUser(any()) }
    }

    @Test
    fun `should delete user`() {
        val userId = 1L

        every { userService.deleteUser(userId) } returns Unit

        mockMvc.perform(delete("/api/users/$userId"))
            .andExpect(status().isOk)

        verify(exactly = 1) { userService.deleteUser(userId) }
    }
}