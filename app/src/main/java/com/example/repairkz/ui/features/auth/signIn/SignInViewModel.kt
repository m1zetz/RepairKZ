package com.example.repairkz.ui.features.auth.signIn

import com.example.repairkz.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.constants.EMAIL_ADDRESS_PATTERN
import com.example.repairkz.common.utils.ValidationResult
import com.example.repairkz.common.utils.Validator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    private val _channel = Channel<SignInEffects>(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

    fun handleIntent(intent: SignInIntent){
        when(intent){
            is SignInIntent.ChangeEmail -> {
                _signInState.value = _signInState.value.copy(email = intent.value, emailError = null)

            }
            is SignInIntent.ChangePassword -> {
                _signInState.value = _signInState.value.copy(password = intent.value, passwordError = null)
            }
            is SignInIntent.SignIn -> {

                val isValid = validateFields(_signInState.value.email, _signInState.value.password)
                if(isValid){
                    // вход
                }
            }
            SignInIntent.NavigateToRegistration -> {
                viewModelScope.launch {
                    _channel.send(SignInEffects.NavigateToRegistration)
                }

            }
        }
    }
    private fun validateFields(email: String, password: String) : Boolean{

        var newState = _signInState.value.copy(emailError = null, passwordError = null)

        val checkEmail = Validator.validateEmail(email)
        val checkPassword = if(password.isEmpty()) R.string.password_empty else null

        newState = newState.copy(
            emailError = (checkEmail as? ValidationResult.Error)?.messageRes,
            passwordError = checkPassword
        )
        _signInState.value = newState
        return checkEmail is ValidationResult.Success && checkPassword == null
    }
}