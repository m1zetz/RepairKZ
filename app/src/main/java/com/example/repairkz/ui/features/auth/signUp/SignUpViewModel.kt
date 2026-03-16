package com.example.repairkz.ui.features.auth.signUp

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import com.example.repairkz.common.utils.ValidationResult
import com.example.repairkz.common.utils.Validator
import com.example.repairkz.data.userData.UserRepository
import com.example.repairkz.domain.useCases.files.SaveToInternalUseCase
import com.example.repairkz.domain.useCases.registration.GetCodeUseCase
import com.example.repairkz.domain.useCases.registration.SendCodeUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserDataUseCase
import com.example.repairkz.ui.features.UserInfo.UserEffects
import com.example.repairkz.ui.features.UserInfo.UserEffects.OpenPhotoPicker
import com.example.repairkz.ui.features.UserInfo.UserState
import com.example.repairkz.ui.features.auth.signUp.SignUpEffect.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.compareTo

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val getCodeUseCase: GetCodeUseCase,
    private val sendCodeUseCase: SendCodeUseCase,
    private val saveToInternalUseCase: SaveToInternalUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
) : ViewModel() {


    private val _uiState = MutableStateFlow(SignUpState())
    val state = _uiState.asStateFlow()

    private val _channel = Channel<SignUpEffect>(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

    private var timerJob: Job? = null
    private fun validationEmail(email: String): Boolean {
        var newState = _uiState.value.copy(emailError = null)

        val checkEmail = Validator.validateEmail(email)


        newState = newState.copy(
            emailError = (checkEmail as? ValidationResult.Error)?.messageRes,

            )
        _uiState.value = newState
        return checkEmail is ValidationResult.Success
    }

    private fun validationCode(code: String): Boolean {
        var newState = _uiState.value.copy(codeError = null)

        val checkCode = Validator.validateCode(code)

        newState = newState.copy(
            codeError = (checkCode as? ValidationResult.Error)?.messageRes
        )
        _uiState.value = newState
        return checkCode is ValidationResult.Success
    }

    private fun validationFirstName(firstName: String): Boolean {
        var newState =
            _uiState.value.copy(userInfo = _uiState.value.userInfo.copy(firstNameError = null))

        val checkFirstName = Validator.validateFirstName(firstName)

        newState = newState.copy(
            userInfo = newState.userInfo.copy(
                firstNameError = (checkFirstName as? ValidationResult.Error)?.messageRes
            )
        )

        _uiState.value = newState

        return checkFirstName is ValidationResult.Success
    }

    private fun validationLastName(lastName: String): Boolean {
        var newState =
            _uiState.value.copy(userInfo = _uiState.value.userInfo.copy(lastNameError = null))

        val checkLastName = Validator.validateLastName(lastName)

        newState = newState.copy(
            userInfo = newState.userInfo.copy(
                lastNameError = (checkLastName as? ValidationResult.Error)?.messageRes
            )
        )
        _uiState.value = newState

        return checkLastName is ValidationResult.Success
    }

    fun handleIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.ChangeEmail -> {
                _uiState.value = _uiState.value.copy(email = intent.emailChar, emailError = null)
            }

            is SignUpIntent.ChangeCode -> {
                if (intent.codeChar.length <= 6) {
                    _uiState.value = _uiState.value.copy(code = intent.codeChar, codeError = null)
                }
            }

            is SignUpIntent.SendEmail -> {
                val isValid = validationEmail(_uiState.value.email)
                if (isValid) {
                    viewModelScope.launch {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            isCodeSent = true
                        )
                        val response = getCodeUseCase(intent.email)
                        response.onSuccess {
                            _channel.send(NavigateToConfirmation)
                            timer()
                        }.onFailure { error ->
                            _channel.send(ShowSnackBar(error.message ?: "Ошибка сети"))
                        }
                        _uiState.value = _uiState.value.copy(
                            isLoading = false
                        )
                    }
                }
            }

            is SignUpIntent.SendCode -> {
                val isValid = validationCode(_uiState.value.code)
                if (isValid) {
                    viewModelScope.launch {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true
                        )
                        delay(3000)
                        val code = intent.code.toInt()
                        val response = sendCodeUseCase(code, _uiState.value.email)
                        response.onSuccess {
                            _channel.send(NavigateToFillingData)
                        }.onFailure { error ->
                            _channel.send(ShowSnackBar(error.message ?: "Ошибка сети"))
                        }
                        _uiState.value = _uiState.value.copy(
                            isLoading = false
                        )

                    }
                }

            }

            SignUpIntent.ResetRegistrationData -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    code = "",
                    error = null
                )
            }

            is SignUpIntent.ChangeFirstName -> {
                _uiState.value = _uiState.value.copy(
                    userInfo = _uiState.value.userInfo.copy(
                        firstName = intent.firstName,
                        firstNameError = null
                    )
                )
            }

            is SignUpIntent.ChangeLastName -> {
                _uiState.value = _uiState.value.copy(
                    userInfo = _uiState.value.userInfo.copy(
                        lastName = intent.lastName,
                        lastNameError = null
                    )
                )
            }

            SignUpIntent.NavigateToMainWindow -> {
                val firstName = _uiState.value.userInfo.firstName
                val lastName = _uiState.value.userInfo.lastName
                val photo = _uiState.value.userInfo.photoUri
                val isFirstNameValid = validationFirstName(firstName)
                val isLastNameValid = validationLastName(lastName)
                if (isFirstNameValid && isLastNameValid) {
                    viewModelScope.launch {

                        updateUserDataUseCase(
                            User(
                                userId = 1,
                                firstName = firstName,
                                lastName = lastName,
                                userPhotoUrl = photo?.toString(),
                                email = _uiState.value.email,
                                phoneNumber = "",
                                status = StatusOfUser.CLIENT,
                                city = CitiesEnum.ALMATY
                            )
                        )
                        _channel.send(NavigateToMainWindow)
                    }

                }

            }

            is SignUpIntent.ChangeAvatar -> {
                viewModelScope.launch {
                    _channel.send(SignUpEffect.OpenPhotoPicker(intent.typeOfSelect))
                }
            }

            SignUpIntent.CloseSheet -> {
                _uiState.update {
                    it.copy(
                        avatarSheetState = false
                    )
                }
            }

            SignUpIntent.OpenSheet -> {
                _uiState.update {
                    it.copy(
                        avatarSheetState = true
                    )
                }
            }

            is SignUpIntent.GetPhotoFromMedia -> {
                _uiState.update { it.copy(userInfo = it.userInfo.copy(pendingPhotoUri = intent.uri)) }

            }

            is SignUpIntent.SelectedPhoto -> {
                viewModelScope.launch {
                    val localUri = saveToInternalUseCase(intent.uri)
                    _uiState.value = _uiState.value.copy(
                        userInfo = _uiState.value.userInfo.copy(
                            photoUri = localUri
                        )
                    )
                }


            }

            SignUpIntent.CancelPhoto -> {
                _uiState.update { it.copy(userInfo = it.userInfo.copy(pendingPhotoUri = null))}

            }


            is SignUpIntent.ConfirmPhoto -> {

                viewModelScope.launch {
                    val localUri = saveToInternalUseCase(intent.uri)
                    _uiState.value = _uiState.value.copy(
                        userInfo = _uiState.value.userInfo.copy(
                            photoUri = localUri,
                            pendingPhotoUri = null
                        )
                    )
                }
            }
        }



    }
    fun timer() {
        _uiState.update { it.copy(timerSeconds = 8) }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timerSeconds > 0) {
                delay(1000)
                _uiState.update {
                    it.copy(timerSeconds = it.timerSeconds - 1)
                }
            }
        }


    }
}


