package com.example.repairkz.ui.features.auth.signUp

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.User
import com.example.repairkz.common.utils.ValidationResult
import com.example.repairkz.common.utils.Validator
import com.example.repairkz.data.local.dataStore.DataStoreManager
import com.example.repairkz.domain.useCases.files.SaveToInternalUseCase
import com.example.repairkz.domain.useCases.auth.CreateUserUseCase
import com.example.repairkz.domain.useCases.auth.GetCodeUseCase
import com.example.repairkz.domain.useCases.auth.SendCodeUseCase
import com.example.repairkz.domain.useCases.files.PreparePhotoPartUseCase
import com.example.repairkz.domain.useCases.userData.SaveUserToLocalUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserDataUseCase
import com.example.repairkz.ui.features.auth.signUp.SignUpEffect.*
import com.google.gson.Gson
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
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
    @ApplicationContext private val context: Context
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

    private fun validationPassword(password: String): Boolean {
        var newState =
            _uiState.value.copy(passwordError = null)

        val checkPassword = Validator.validatePassword(password)

        newState = newState.copy(
            passwordError = (checkPassword as? ValidationResult.Error)?.messageRes
        )

        _uiState.value = newState

        return checkPassword is ValidationResult.Success
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
                val password = _uiState.value.password
                val photo = _uiState.value.userInfo.photoUri
                val isFirstNameValid = validationFirstName(firstName)
                val isLastNameValid = validationLastName(lastName)
                val isPasswordValid = validationPassword(password)
                if (isFirstNameValid && isLastNameValid && isPasswordValid) {
                    var user = User(
                        id = 0,
                        firstName = firstName.replaceFirstChar { it.uppercase() },
                        lastName = lastName.replaceFirstChar { it.uppercase() },
                        userPhotoUrl = photo?.toString(),
                        email = _uiState.value.email,
                        password = password,
                        phoneNumber = "",
                        status = StatusOfUser.CLIENT,
                        city = CitiesEnum.ALMATY
                    )
                    viewModelScope.launch {
                        _uiState.update { state ->
                            state.copy(isLoading = true)
                        }
                        try {
                            val uri = _uiState.value.userInfo.photoUri
                            val photoPart = if(uri!=null) preparePhotoPartUseCase(context,uri) else null

                            val userPart = user.toCreateUserDTO()
                            val response = createUserUseCase(userPart,photoPart )
                            response.onSuccess { dto ->
                                val finalUser = user.copy(userId = dto.id)
                                dataStoreManager.saveToken(dto.token)
                                saveUserToLocalUseCase(finalUser)
                                _channel.send(NavigateToMainWindow)
                            }.onFailure { error ->
                                _uiState.update { it.copy(error = error.message) }
                            }

                        } catch(e: Exception){
                            _uiState.update { it.copy(error = "Ошибка сети") }
                        } finally {
                            _uiState.update { state ->
                                state.copy(isLoading = false)
                            }
                        }

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

            SignUpIntent.CancelPhoto -> {
                _uiState.update { it.copy(userInfo = it.userInfo.copy(pendingPhotoUri = null)) }

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

            is SignUpIntent.ChangePassword -> {
                _uiState.value = _uiState.value.copy(
                    password = intent.password,
                    passwordError = null
                )
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


