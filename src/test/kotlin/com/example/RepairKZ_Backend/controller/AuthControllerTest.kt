package com.example.RepairKZ_Backend.controller

import com.example.RepairKZ_Backend.common.enums.CitiesEnum
import com.example.RepairKZ_Backend.common.enums.StatusOfUser
import com.example.RepairKZ_Backend.model.UserRegistrationDTO
import com.example.RepairKZ_Backend.model.RegistrationResponseDTO
import com.example.RepairKZ_Backend.model.LoginRequestDTO
import com.example.RepairKZ_Backend.model.LoginResponseDTO
import com.example.RepairKZ_Backend.service.AuthService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebMvcTest(AuthController::class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var authService: AuthService

    @Test
    @WithMockUser
    fun `should register new user`() {
        val registrationDto = UserRegistrationDTO(
            firstName = "Nurlan",
            lastName = "Saburov",
            email = "nurlan@example.kz",
            password = "securePassword",
            phone = "7071234567",
            city = CitiesEnum.KARAGANDY,
            status = StatusOfUser.MASTER
        )

        val expectedResponse = RegistrationResponseDTO(id = 100L, token = "fake-jwt-token")

        every { authService.register(any()) } returns expectedResponse

        mockMvc.perform(
            post("/api/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").value("fake-jwt-token"))
    }
}