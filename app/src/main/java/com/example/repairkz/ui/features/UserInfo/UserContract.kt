package com.example.repairkz.ui.features.UserInfo

import android.net.Uri
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.PhotoSourceEnum
import com.example.repairkz.common.models.User
import com.example.repairkz.data.remote.dto.MasterServiceDTO

data class UserState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val error: String? = null,
    val newAvatarData: Uri? = null,

    val numberDraft: String = "",
    val cityDraft: CitiesEnum = CitiesEnum.UNKNOWN,
    val emailDraft: String = "",
    val descriptionDraft: String = "",
    val experienceDraft: String = "",
    val specDraft: MasterSpetializationsEnum = MasterSpetializationsEnum.UNKNOWN,
    val serviceDraft: String = "",
    val priceDraft: String = "",

    val avatarSheetState: Boolean = false,
    val pendingUri: Uri? = null,
    val isPhotoSaving: Boolean = false,
    val showSave: Boolean = false,
    val showCreate: Boolean = false,
)

data class BusinessCardData(
    val id: Long,
    val photoUrl: String,
    val firstName: String,
    val lastName: String,
    val isMe: Boolean,
)

sealed class UserIntent {
    object OpenSheet : UserIntent()
    object CloseSheet : UserIntent()
    data class ChangeAvatar(val typeOfSelect: PhotoSourceEnum) : UserIntent()
    data class ChangeNumber(val number: String) : UserIntent()
    data class ChangeCity(val city: CitiesEnum) : UserIntent()
    data class ChangeEmail(val email: String) : UserIntent()
    data class ConfirmPhoto(val uri: Uri) : UserIntent()
    object CancelPhoto : UserIntent()
    data class GetPhotoFromMedia(val uri: Uri) : UserIntent()
    object SaveChanges : UserIntent()

    sealed class CurrentMasterIntent : UserIntent() {
        data class ChangeDescription(val description: String) : CurrentMasterIntent()
        data class ChangeExperience(val experience: String) : CurrentMasterIntent()
        data class ChangeSpecialization(val spec: MasterSpetializationsEnum) : CurrentMasterIntent()

        object CreateService : CurrentMasterIntent()
        data class DeleteService(val id: Long) : CurrentMasterIntent()
        data class UpdateService(val dto: MasterServiceDTO) : CurrentMasterIntent()
        data class ChangeServiceDraft(val value: String) : CurrentMasterIntent()
        data class ChangePriceDraft(val value: String) : CurrentMasterIntent()

        object CloseCreate : CurrentMasterIntent()
        object OpenCreate : CurrentMasterIntent()

    }
}


sealed interface UserEffects {
    object MapsToPreview : UserEffects

    data class OpenPhotoPicker(val typeOfSelect: PhotoSourceEnum) : UserEffects
    data class NavigateToOrderRegistration(val masterId: Long) : UserEffects
}