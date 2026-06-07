package com.example.repairkz.ui.features.signIn

import com.example.repairkz.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import com.example.repairkz.common.utils.ValidationResult
import com.example.repairkz.common.utils.Validator
import com.example.repairkz.data.local.dataStore.DataStoreManager
import com.example.repairkz.data.remote.dto.LoginDTO
import com.example.repairkz.domain.errors.AuthorizationError
import com.example.repairkz.domain.useCases.auth.LoginUseCase
import com.example.repairkz.domain.useCases.userData.SaveUserToLocalUseCase
import com.example.repairkz.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val dataStoreManager: DataStoreManager,
    private val saveUserToLocalUseCase: SaveUserToLocalUseCase
)  : BaseViewModel<SignInState, SignInIntent, SignInEffect>() {
    override val initialState = SignInState()




    override fun handleIntent(intent: SignInIntent){
        when(intent){
            is SignInIntent.ChangeEmail -> {
                setState {
                    copy(email = intent.value, emailError = null)
                }

            }
            is SignInIntent.ChangePassword -> {
                setState {
                    copy(password = intent.value, passwordError = null)
                }
            }
            is SignInIntent.SignIn -> {
                val state = _state.value
                val isValid = validateFields(state.email, state.password)
                if(isValid){
                    viewModelScope.launch {
                        try {
                            setState {
                                copy(
                                    isLoading = true
                                )
                            }

                            val response = loginUseCase(LoginDTO(state.email, state.password))

                            response.onSuccess {loginResponseDTO ->
                                val dto = loginResponseDTO.user
                                val user = User(
                                    id = loginResponseDTO.id,
                                    userPhotoUrl = dto.userPhotoUrl,
                                    firstName = dto.firstName,
                                    lastName = dto.lastName,
                                    email = dto.email,
                                    phoneNumber = dto.phone,
                                    status = dto.status,
                                    city = dto.city
                                )
                                saveUserToLocalUseCase(
                                    when(loginResponseDTO.user.status){
                                        StatusOfUser.CLIENT -> {
                                            user
                                        }
                                        StatusOfUser.MASTER -> {
                                            user.toMasterWithData(
                                                masterId = loginResponseDTO.master?.masterId ?: 0,
                                                spec = loginResponseDTO.master?.masterSpecialization ?: MasterSpetializationsEnum.UNKNOWN,
                                                desc = loginResponseDTO.master?.description ?: "",
                                                exp = loginResponseDTO.master?.experienceInYears ?: 0,
                                                services = loginResponseDTO.master?.services?.map {
                                                    it.toModel()
                                                } ?: emptyList()
                                            )
                                        }
                                    }

                                )
                                dataStoreManager.saveToken(loginResponseDTO.token)
                                sendEffect(SignInEffect.NavigateToMainWindow)
                            }.onFailure { error ->
                                if(error is AuthorizationError){
                                    sendEffect(SignInEffect.ShowSnackBar(error))
                                }

                            }
                        } catch (e: Exception){
                            setState {
                                copy(error = "Ошибка сети")
                            }

                        } finally {
                            setState {
                                copy(
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
            }
            SignInIntent.NavigateToRegistration -> {
                sendEffect(SignInEffect.NavigateToRegistration)

            }
        }
    }
    private fun validateFields(email: String, password: String) : Boolean{
        setState { copy(emailError = null, passwordError = null) }

        val checkEmail = Validator.validateEmail(email)
        val checkPassword = if(password.isEmpty()) R.string.password_empty else null

        setState {
            copy(
                emailError = (checkEmail as? ValidationResult.Error)?.messageRes,
                passwordError = checkPassword
            )
        }
        return checkEmail is ValidationResult.Success && checkPassword == null
    }
}