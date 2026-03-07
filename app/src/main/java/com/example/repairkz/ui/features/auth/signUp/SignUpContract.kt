package com.example.repairkz.ui.features.auth.signUp

data class SignUpState(
    val email: String = "",
    val code: String = "",
    val emailError: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class SignUpIntent{
    data class SendEmail(val email: String) : SignUpIntent()
    data class SendCode(val code: String) : SignUpIntent()
    data class ChangeEmail(val emailChar: String) : SignUpIntent()
    data class ChangeCode(val codeChar: String) : SignUpIntent()
}

sealed class SignUpEffect{
    object NavigateToConfirmation : SignUpEffect()
    data class ShowSnackBar(val message: String) : SignUpEffect()
}