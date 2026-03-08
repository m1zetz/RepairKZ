package com.example.repairkz.ui.features.auth.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.utils.ValidationResult
import com.example.repairkz.common.utils.Validator
import com.example.repairkz.domain.useCases.registration.GetCodeUseCase
import com.example.repairkz.domain.useCases.registration.SendCodeUseCase
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

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val getCodeUseCase: GetCodeUseCase,
    private val sendCodeUseCase: SendCodeUseCase
)  : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpState())
    val state = _uiState.asStateFlow()

    private val _channel = Channel<SignUpEffect>(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

    private var timerJob: Job? = null
    private fun validationEmail(email:String) : Boolean{
        var newState = _uiState.value.copy(emailError = null)

        val checkEmail = Validator.validateEmail(email)


        newState = newState.copy(
            emailError = (checkEmail as? ValidationResult.Error)?.messageRes,

        )
        _uiState.value = newState
        return checkEmail is ValidationResult.Success
    }

    fun handleIntent(intent: SignUpIntent){
        when(intent){
            is SignUpIntent.ChangeEmail -> {
                _uiState.value = _uiState.value.copy(email = intent.emailChar, emailError = null)
            }
            is SignUpIntent.ChangeCode -> {
                if(intent.codeChar.length <= 6){
                    _uiState.value = _uiState.value.copy(code = intent.codeChar)
                }
            }
            is SignUpIntent.SendEmail -> {
                val isValid = validationEmail(_uiState.value.email)
                if(isValid){
                    viewModelScope.launch {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            isCodeSent = true
                        )
                        val response = getCodeUseCase(intent.email)
                        response.onSuccess {
                            _channel.send(NavigateToConfirmation)
                            timer()
                        }.onFailure {error ->
                            _channel.send(ShowSnackBar(error.message?: "Ошибка сети"))
                        }
                        _uiState.value = _uiState.value.copy(
                            isLoading = false
                        )

                    }

                }
            }

            is SignUpIntent.SendCode -> {
                viewModelScope.launch {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true
                    )
                    val code = intent.code.toInt()
                    val response = sendCodeUseCase(code, _uiState.value.email)
                    response.onSuccess {
                        _channel.send(NavigateToFillingData)
                    }.onFailure {error ->
                        _channel.send(ShowSnackBar(error.message?: "Ошибка сети"))
                    }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )

                }
            }

            SignUpIntent.ResetRegistrationData -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    code = "",
                    error = null
                )
            }
        }
    }

    fun timer(){
        _uiState.update { it.copy(timerSeconds = 8) }
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_uiState.value.timerSeconds > 0){
                delay(1000)
                _uiState.update {
                    it.copy(timerSeconds = it.timerSeconds-1)
                }
            }
        }


    }


}