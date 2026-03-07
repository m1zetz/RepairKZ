package com.example.repairkz.ui.features.auth.signUp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.utils.ValidationResult
import com.example.repairkz.common.utils.Validator
import com.example.repairkz.ui.features.auth.signUp.SignUpEffect.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class SignUpViewModel  : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpState())
    val state = _uiState.asStateFlow()

    private val _channel = Channel<SignUpEffect>(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

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
                            isLoading = true
                        )
                        try {
                            //отправка на код
                            //throw Exception("epwpfp")
                            _uiState.value = _uiState.value.copy(
                                isLoading = false
                            )
                            _channel.send(NavigateToConfirmation)
                        } catch (e: Exception){
                            _channel.send(ShowSnackBar("Ошибка запроса"))
                        }

                    }

                }
            }

            is SignUpIntent.SendCode -> {
                viewModelScope.launch {
                    _uiState.value = _uiState.value.copy(
                        isLoading = true
                    )
                    try {
                        //отправка на проверку
                        delay(3000)
                        throw Exception("неверный код")
                    } catch (e: Exception){
                        _channel.send(ShowSnackBar("неверный код"))
                    }

                }
            }
        }
    }

}