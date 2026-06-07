package com.example.repairkz.ui.features.signIn

import com.example.repairkz.domain.errors.AuthorizationError
import com.example.repairkz.ui.base.UiEffect
import com.example.repairkz.ui.base.UiIntent
import com.example.repairkz.ui.base.UiState

data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState

sealed class SignInIntent  : UiIntent{
    data class ChangeEmail(val value: String) : SignInIntent()
    data class ChangePassword(val value: String) : SignInIntent()
    data class SignIn(val email: String, val password: String) : SignInIntent()

    object NavigateToRegistration : SignInIntent()

}

sealed class SignInEffect: UiEffect{
    data class ShowSnackBar(val error: AuthorizationError) : SignInEffect()
    object NavigateToMainWindow : SignInEffect()
    object NavigateToRegistration : SignInEffect()
}