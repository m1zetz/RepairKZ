package com.example.repairkz.ui.features.UserInfo

import android.net.Uri
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.PhotoSourceEnum
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User
import com.example.repairkz.ui.features.auth.signUp.SignUpIntent

sealed class UserState {
    object Loading : UserState()
    data class Success(
        val userTypes: UserTypes,
        val avatarSheetState: Boolean = false,
        val newAvatarData: Uri? = null,
        val pendingUri: Uri? = null,

        val number: String = "",
        val city: String = "",
        val email: String = "",


        val descriptionDraft: String = "",
        val experienceDraft: String = "",
        val specDraft: MasterSpetializationsEnum = MasterSpetializationsEnum.UNKNOWN
    ) : UserState()

    data class Error(val message: String) : UserState()
}

data class CommonInfo(
    val id: Long,
    val photoUrl: String,
    val firstName: String,
    val lastName: String,
    val isMe: Boolean,
    )

sealed class UserTypes {

    abstract val commonInfo: CommonInfo

    data class IsCurrentUser(val user: User, override val commonInfo: CommonInfo) : UserTypes()
    data class IsCurrentMaster(val master: Master, override val commonInfo: CommonInfo) :
        UserTypes()

    data class IsOtherMaster(val master: Master, override val commonInfo: CommonInfo) : UserTypes()
}

sealed class UserIntent {


    object OpenSheet : UserIntent()
    object CloseSheet : UserIntent()
    data class ChangeAvatar(val typeOfSelect: PhotoSourceEnum) : UserIntent()
    data class ChangeNumber(val number: String) : UserIntent()
    data class ChangeCity(val city: String) : UserIntent()
    data class ChangeEmail(val email: String) : UserIntent()
    data class ConfirmPhoto(val uri: Uri) : UserIntent()
    object CancelPhoto : UserIntent()
    data class GetPhotoFromMedia(val uri: Uri) : UserIntent()
    sealed class MasterProfileIntent : UserIntent() {
        data class DoOrder(val masterId: Long) : MasterProfileIntent()
        data class AddToFavorites(val masterId: Long) : MasterProfileIntent()
        data class Report(val masterId: Long) : MasterProfileIntent()
    }

    sealed class CurrentMasterIntent : UserIntent() {
        data class ChangeDescription(val description: String) : CurrentMasterIntent()
        data class ChangeExperience(val experience: String) : CurrentMasterIntent()
        data class ChangeSpecialization(val spec: MasterSpetializationsEnum) : CurrentMasterIntent()

        object SaveDescription : CurrentMasterIntent()
        object SaveExperience : CurrentMasterIntent()
        object SaveSpecialization : CurrentMasterIntent()

    }
}


sealed interface UserEffects {
    object MapsToPreview: UserEffects

    data class OpenPhotoPicker(val typeOfSelect: PhotoSourceEnum) : UserEffects
    data class NavigateToOrderRegistration(val masterId: Long) : UserEffects
}