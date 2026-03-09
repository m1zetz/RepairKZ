package com.example.repairkz.ui.features.auth.signUp

import android.net.Uri

data class SignUpState(
    val email: String = "",
    val code: String = "",
    val emailError: Int? = null,
    val codeError: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCodeSent: Boolean = false,
    val timerSeconds: Int = 0,
    val canResendCode: Boolean = true,
    val userInfo: MinimalUserInfo = MinimalUserInfo()
)

data class MinimalUserInfo(
    val photoUri: Uri? = null,
    val firstName: String = "",
    val lastName: String = ""
)

sealed class SignUpIntent{
    data class SendEmail(val email: String) : SignUpIntent()
    data class SendCode(val code: String) : SignUpIntent()
    data class ChangeEmail(val emailChar: String) : SignUpIntent()
    data class ChangeCode(val codeChar: String) : SignUpIntent()
    object ResetRegistrationData : SignUpIntent()
}

sealed class SignUpEffect{
    object NavigateToConfirmation : SignUpEffect()
    object NavigateToFillingData : SignUpEffect()
    object NavigateToMainWindow : SignUpEffect()
    data class ShowSnackBar(val message: String) : SignUpEffect()

}