package com.example.repairkz.ui.features.signUp

import android.net.Uri
import com.example.repairkz.common.enums.PhotoSourceEnum
import com.example.repairkz.domain.errors.AuthorizationError
import com.example.repairkz.ui.base.UiEffect
import com.example.repairkz.ui.base.UiIntent
import com.example.repairkz.ui.base.UiState

data class SignUpState(
    val email: String = "",
    val code: String = "",
    val password: String = "",
    val passwordError: Int? = null,
    val emailError: Int? = null,
    val codeError: Int? = null,
    val isLoading: Boolean = false,
    val isPhotoLoading: Boolean = false,
    val error: String? = null,
    val isCodeSent: Boolean = false,
    val timerSeconds: Int = 0,
    val canResendCode: Boolean = true,
    val userInfo: MinimalUserInfo = MinimalUserInfo(),
    val avatarSheetState: Boolean = false
) : UiState

data class MinimalUserInfo(
    val photoUri: Uri? = null,
    val pendingPhotoUri: Uri? = null,
    val firstName: String = "",
    val lastName: String = "",
    val firstNameError: Int? = null,
    val lastNameError: Int? = null
)

sealed class SignUpIntent : UiIntent{
    data class SendEmail(val email: String) : SignUpIntent()
    data class SendCode(val code: String) : SignUpIntent()
    data class ChangeEmail(val emailChar: String) : SignUpIntent()
    data class ChangeCode(val codeChar: String) : SignUpIntent()
    data class ChangePassword(val password: String) : SignUpIntent()

    data class ChangeFirstName(val firstName: String) : SignUpIntent()
    data class ChangeLastName(val lastName: String) : SignUpIntent()
    object ResetRegistrationData : SignUpIntent()
    object NavigateToMainWindow : SignUpIntent()

    data class ChangeAvatar(val typeOfSelect: PhotoSourceEnum) : SignUpIntent()
    object OpenSheet : SignUpIntent()
    object CloseSheet: SignUpIntent()
    data class ConfirmPhoto(val uri: Uri) : SignUpIntent()
    object CancelPhoto : SignUpIntent()
    data class GetPhotoFromMedia(val uri: Uri?) : SignUpIntent()
}

sealed class SignUpEffect : UiEffect{
    object NavigateToConfirmation : SignUpEffect()

    object NavigateToFillingData : SignUpEffect()
    object NavigateToMainWindow : SignUpEffect()
    data class ShowSnackBar(val error: AuthorizationError) : SignUpEffect()

    data class OpenPhotoPicker(val typeOfSelect: PhotoSourceEnum) : SignUpEffect()

}