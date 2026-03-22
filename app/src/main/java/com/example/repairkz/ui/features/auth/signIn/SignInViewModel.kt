package com.example.repairkz.ui.features.auth.signIn

import android.util.Log
import com.example.repairkz.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.constants.EMAIL_ADDRESS_PATTERN
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User
import com.example.repairkz.common.utils.ValidationResult
import com.example.repairkz.common.utils.Validator
import com.example.repairkz.data.local.dataStore.DataStoreManager
import com.example.repairkz.data.remote.api.TestApi
import com.example.repairkz.data.remote.dto.LoginDTO
import com.example.repairkz.data.remote.dto.UserResponseDTO
import com.example.repairkz.domain.useCases.auth.LoginUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserDataUseCase
import com.example.repairkz.ui.features.auth.signUp.SignUpEffect.ShowSnackBar
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
    private val updateUserDataUseCase : UpdateUserDataUseCase,
    private val dataStoreManager: DataStoreManager,
    private val testApi: TestApi
)  : ViewModel() {
    private val _signInState = MutableStateFlow(SignInState())
    val signInState = _signInState.asStateFlow()

    private val _channel = Channel<SignInEffects>(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

    init{
        viewModelScope.launch {
            try {
                val response = testApi.hello()
                if(response.isSuccessful){
                    Log.d("",response.body() ?: "")
                }
                else{
                    Log.d("Test", "Ошибка: ${response.code()}")
                }
            } catch (e: Exception){
                Log.e("Test", "Exception: ${e.message} ${e.cause}")
            }
        }


    }

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
                    viewModelScope.launch {
                        try {
                            _signInState.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                            val response = loginUseCase(LoginDTO(_signInState.value.email, _signInState.value.password))
                            response.onSuccess {loginResponseDTO ->
                                val dto = loginResponseDTO.user
                                val user = User(
                                    id = loginResponseDTO.id.toInt(),
                                    userPhotoUrl = dto.userPhotoUrl,
                                    firstName = dto.firstName,
                                    lastName = dto.lastName,
                                    email = dto.email,
                                    phoneNumber = dto.phone,
                                    status = dto.status,
                                    city = dto.city
                                )
                                updateUserDataUseCase(
                                    when(loginResponseDTO.user.status){
                                        StatusOfUser.CLIENT -> {
                                            user
                                        }
                                        StatusOfUser.MASTER -> {
                                            user.toMaster().copy(
                                                masterSpecialization = loginResponseDTO.master?.masterSpecialization,
                                                description = loginResponseDTO.master?.description,
                                                experienceInYears = loginResponseDTO.master?.experienceInYears
                                            )
                                        }
                                    }

                                )
                                dataStoreManager.saveToken(loginResponseDTO.token)
                                _channel.send(SignInEffects.NavigateToMainWindow)
                            }.onFailure {error ->
                                _channel.send(SignInEffects.ShowSnackBar(error.message ?: "Ошибка сети"))
                            }
                        } catch (e: Exception){

                            _signInState.update { it.copy(error = "Ошибка сети") }

                        } finally {
                            _signInState.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                        }
                    }
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