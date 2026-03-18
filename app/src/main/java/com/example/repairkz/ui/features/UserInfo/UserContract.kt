package com.example.repairkz.ui.features.UserInfo

import android.net.Uri
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
        val pendingUri: Uri? = null
    ) : UserState()

    data class Error(val message: String) : UserState()
}

data class CommonInfo(
    val id: Int,
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

    data class ConfirmPhoto(val uri: Uri) : UserIntent()
    object CancelPhoto : UserIntent()
    data class GetPhotoFromMedia(val uri: Uri) : UserIntent()
    sealed class MasterProfileIntent : UserIntent() {
        data class DoOrder(val masterId: Int) : MasterProfileIntent()
        data class AddToFavorites(val masterId: Int) : MasterProfileIntent()
        data class Report(val masterId: Int) : MasterProfileIntent()
    }
}

sealed interface UserEffects {
    object MapsToPreview: UserEffects

    data class OpenPhotoPicker(val typeOfSelect: PhotoSourceEnum) : UserEffects
}