package com.example.repairkz.ui.features.signUp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import com.example.repairkz.common.utils.ValidationResult
import com.example.repairkz.common.utils.Validator
import com.example.repairkz.data.local.dataStore.DataStoreManager
import com.example.repairkz.domain.errors.AuthorizationError
import com.example.repairkz.domain.useCases.files.SaveToInternalUseCase
import com.example.repairkz.domain.useCases.auth.CreateUserUseCase
import com.example.repairkz.domain.useCases.auth.GetCodeUseCase
import com.example.repairkz.domain.useCases.auth.SendCodeUseCase
import com.example.repairkz.domain.useCases.files.PreparePhotoPartUseCase
import com.example.repairkz.domain.useCases.userData.SaveUserToLocalUseCase
import com.example.repairkz.ui.base.BaseViewModel
import com.example.repairkz.ui.features.signUp.SignUpEffect.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val getCodeUseCase: GetCodeUseCase,
    private val sendCodeUseCase: SendCodeUseCase,
    private val saveToInternalUseCase: SaveToInternalUseCase,
    private val saveUserToLocalUseCase: SaveUserToLocalUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val dataStoreManager: DataStoreManager,
    private val preparePhotoPartUseCase: PreparePhotoPartUseCase,
    @ApplicationContext private val context: Context,
) : BaseViewModel<SignUpState, SignUpIntent, SignUpEffect>() {


    override val initialState = SignUpState()

    private var timerJob: Job? = null
    private fun validationEmail(email: String): Boolean {
        setState {
            copy(emailError = null)
        }

        val checkEmail = Validator.validateEmail(email)


        setState {
            copy(
                emailError = (checkEmail as? ValidationResult.Error)?.messageRes,

                )
        }
        return checkEmail is ValidationResult.Success
    }

    private fun validationCode(code: String): Boolean {
        setState {
            copy(codeError = null)
        }

        val checkCode = Validator.validateCode(code)

        setState {
            copy(
                codeError = (checkCode as? ValidationResult.Error)?.messageRes
            )
        }
        return checkCode is ValidationResult.Success
    }

    private fun validationFirstName(firstName: String): Boolean {
        setState {
            copy(userInfo = userInfo.copy(firstNameError = null))
        }

        val checkFirstName = Validator.validateFirstName(firstName)

        setState {
            copy(
                userInfo = userInfo.copy(
                    firstNameError = (checkFirstName as? ValidationResult.Error)?.messageRes
                )
            )
        }

        return checkFirstName is ValidationResult.Success
    }

    private fun validationLastName(lastName: String): Boolean {
        setState {
            copy(userInfo = userInfo.copy(lastNameError = null))
        }

        val checkLastName = Validator.validateLastName(lastName)

        setState {
            copy(
                userInfo = userInfo.copy(
                    lastNameError = (checkLastName as? ValidationResult.Error)?.messageRes
                )
            )
        }

        return checkLastName is ValidationResult.Success
    }

    private fun validationPassword(password: String): Boolean {
        setState {
            copy(passwordError = null)
        }

        val checkPassword = Validator.validatePassword(password)

        setState {
            copy(
                passwordError = (checkPassword as? ValidationResult.Error)?.messageRes
            )
        }
        return checkPassword is ValidationResult.Success
    }

    override fun handleIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.ChangeEmail -> {
                setState {
                    copy(email = intent.emailChar, emailError = null)
                }
            }

            is SignUpIntent.ChangeCode -> {
                if (intent.codeChar.length <= 6) {
                    setState {
                        copy(code = intent.codeChar, codeError = null)
                    }
                }
            }

            is SignUpIntent.SendEmail -> {
                val isValid = validationEmail(_state.value.email)
                if (isValid) {
                    viewModelScope.launch {
                        setState {
                            copy(
                                isLoading = true,
                                isCodeSent = true
                            )
                        }
                        val response = getCodeUseCase(intent.email)
                        response.onSuccess {
                            sendEffect(NavigateToConfirmation)
                            timer()
                        }.onFailure { error ->
                            if (error is AuthorizationError) {
                                sendEffect(ShowSnackBar(error))
                            }

                        }
                        setState {
                            copy(
                                isLoading = false
                            )

                        }
                    }
                }
            }

            is SignUpIntent.SendCode -> {
                val isValid = validationCode(_state.value.code)
                if (isValid) {
                    viewModelScope.launch {
                        setState {
                            copy(
                                isLoading = true
                            )
                        }
                        val code = intent.code.toInt()
                        val response = sendCodeUseCase(code, _state.value.email)
                        response.onSuccess {
                            sendEffect(NavigateToFillingData)
                        }.onFailure { error ->
                            if (error is AuthorizationError) {
                                sendEffect(ShowSnackBar(error))
                            }
                        }
                        setState {
                            copy(
                                isLoading = false
                            )
                        }

                    }
                }

            }

            SignUpIntent.ResetRegistrationData -> {
                setState {
                    copy(
                        isLoading = false,
                        code = "",
                        error = null
                    )
                }
            }

            is SignUpIntent.ChangeFirstName -> {
                setState {
                    copy(
                        userInfo = userInfo.copy(
                            firstName = intent.firstName,
                            firstNameError = null
                        )
                    )
                }
            }

            is SignUpIntent.ChangeLastName -> {
                setState {
                    copy(
                        userInfo = userInfo.copy(
                            lastName = intent.lastName,
                            lastNameError = null
                        )
                    )
                }
            }

            SignUpIntent.NavigateToMainWindow -> {
                val state = _state.value
                val firstName = state.userInfo.firstName
                val lastName = state.userInfo.lastName
                val password = state.password
                val photo = state.userInfo.photoUri
                val isFirstNameValid = validationFirstName(firstName)
                val isLastNameValid = validationLastName(lastName)
                val isPasswordValid = validationPassword(password)
                if (isFirstNameValid && isLastNameValid && isPasswordValid) {
                    val user = User(
                        id = 0,
                        firstName = firstName.replaceFirstChar { it.uppercase() },
                        lastName = lastName.replaceFirstChar { it.uppercase() },
                        userPhotoUrl = photo?.toString(),
                        email = state.email,
                        password = password,
                        phoneNumber = "",
                        status = StatusOfUser.CLIENT,
                        city = CitiesEnum.ALMATY
                    )
                    viewModelScope.launch {
                        setState {
                            copy(isLoading = true)
                        }
                        try {
                            val uri = _state.value.userInfo.photoUri
                            val photoPart =
                                if (uri != null) preparePhotoPartUseCase(context, uri) else null

                            val userPart = user.toCreateUserDTO()
                            val response = createUserUseCase(userPart, photoPart)
                            response.onSuccess { dto ->
                                val finalUser = user.copy(userId = dto.id)
                                dataStoreManager.saveToken(dto.token)
                                saveUserToLocalUseCase(finalUser)
                                sendEffect(NavigateToMainWindow)
                            }.onFailure { error ->
                                setState {
                                    copy(error = error.message)
                                }
                            }

                        } catch (e: Exception) {
                            setState {
                                copy(error = "Ошибка сети")
                            }
                        } finally {
                            setState {
                                copy(isLoading = false)
                            }
                        }

                    }

                }

            }

            is SignUpIntent.ChangeAvatar -> {
                sendEffect(OpenPhotoPicker(intent.typeOfSelect))
            }

            SignUpIntent.CloseSheet -> {
                setState {
                    copy(
                        avatarSheetState = false
                    )
                }
            }

            SignUpIntent.OpenSheet -> {
                setState {
                    copy(
                        avatarSheetState = true
                    )
                }
            }

            is SignUpIntent.GetPhotoFromMedia -> {
                setState { copy(userInfo = userInfo.copy(pendingPhotoUri = intent.uri)) }

            }

            SignUpIntent.CancelPhoto -> {
                setState {
                    copy(userInfo = userInfo.copy(pendingPhotoUri = null))
                }

            }


            is SignUpIntent.ConfirmPhoto -> {

                viewModelScope.launch {
                    setState {
                        copy(
                            isPhotoLoading = true,
                            userInfo = userInfo.copy(pendingPhotoUri = null)
                        )
                    }
                    val localUri = saveToInternalUseCase(intent.uri)
                    setState {
                        copy(
                            userInfo = userInfo.copy(
                                photoUri = localUri,
                            ),
                            isPhotoLoading = false
                        )
                    }
                }
            }

            is SignUpIntent.ChangePassword -> {
                setState {
                    copy(
                        password = intent.password,
                        passwordError = null
                    )
                }
            }
        }


    }

    fun timer() {
        setState {
            copy(timerSeconds = 60)
        }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_state.value.timerSeconds > 0) {
                delay(1000)
                setState {
                    copy(timerSeconds = timerSeconds - 1)
                }
            }
        }


    }
}


