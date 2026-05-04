package com.example.repairkz.ui.features.auth.signIn

import com.example.repairkz.domain.errors.AuthorizationError

data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class SignInIntent{
    data class ChangeEmail(val value: String) : SignInIntent()
    data class ChangePassword(val value: String) : SignInIntent()
    data class SignIn(val email: String, val password: String) : SignInIntent()

    object NavigateToRegistration : SignInIntent()

}

sealed class SignInEffects{
    data class ShowSnackBar(val error: AuthorizationError) : SignInEffects()
    object NavigateToMainWindow : SignInEffects()
    object NavigateToRegistration : SignInEffects()
}