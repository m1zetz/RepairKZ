package com.example.repairkz.ui.features.auth.signUp

import android.net.Uri
import com.example.repairkz.common.enums.PhotoSourceEnum
import com.example.repairkz.ui.features.UserInfo.UserEffects
import com.example.repairkz.ui.features.UserInfo.UserIntent

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
    val userInfo: MinimalUserInfo = MinimalUserInfo(),
    val avatarSheetState: Boolean = false
)

data class MinimalUserInfo(
    val photoUri: Uri? = null,
    val firstName: String = "",
    val lastName: String = "",
    val firstNameError: Int? = null,
    val lastNameError: Int? = null
)

sealed class SignUpIntent{
    data class SendEmail(val email: String) : SignUpIntent()
    data class SendCode(val code: String) : SignUpIntent()
    data class ChangeEmail(val emailChar: String) : SignUpIntent()
    data class ChangeCode(val codeChar: String) : SignUpIntent()

    data class ChangeFirstName(val firstName: String) : SignUpIntent()
    data class ChangeLastName(val lastName: String) : SignUpIntent()
    object ResetRegistrationData : SignUpIntent()
    object NavigateToMainWindow : SignUpIntent()

    data class ChangeAvatar(val typeOfSelect: PhotoSourceEnum) : SignUpIntent()
    object OpenSheet : SignUpIntent()
    object CloseSheet: SignUpIntent()

    data class SelectedPhoto(val uri: Uri) : SignUpIntent()
    data class GetPhotoFromMedia(val uri: Uri?) : SignUpIntent()
}

sealed class SignUpEffect{
    object NavigateToConfirmation : SignUpEffect()
    object NavigateToPreview : SignUpEffect()
    object NavigateToFillingData : SignUpEffect()
    object NavigateToMainWindow : SignUpEffect()
    data class ShowSnackBar(val message: String) : SignUpEffect()

    data class OpenPhotoPicker(val typeOfSelect: PhotoSourceEnum) : SignUpEffect()

}