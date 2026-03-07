package com.example.repairkz.ui.features.auth.signIn

data class SignInState(
    val email: String = "",
    val password: String = "",
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val isLoading: Boolean = false
)

sealed class SignInIntent{
    data class ChangeEmail(val value: String) : SignInIntent()
    data class ChangePassword(val value: String) : SignInIntent()
    data class SignIn(val email: String, val password: String) : SignInIntent()

    object NavigateToRegistration : SignInIntent()

}

sealed class SignInEffects{
    object NavigateToRegistration : SignInEffects()
}