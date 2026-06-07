package com.example.repairkz.ui.features.UserInfo

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.enums.CitiesEnum
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User
import com.example.repairkz.data.remote.dto.MasterServiceDTO
import com.example.repairkz.domain.useCases.files.PreparePhotoPartUseCase
import com.example.repairkz.domain.useCases.files.SaveToInternalUseCase
import com.example.repairkz.domain.useCases.services.CreateServiceUseCase
import com.example.repairkz.domain.useCases.services.UpdateServiceUseCase
import com.example.repairkz.domain.useCases.services.DeleteServiceUseCase
import com.example.repairkz.domain.useCases.services.GetServicesUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserPhotoUseCase
import com.example.repairkz.ui.base.BaseViewModel
import com.example.repairkz.ui.features.UserInfo.UserEffect.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val saveToInternalUseCase: SaveToInternalUseCase,
    private val updateUserPhotoUseCase: UpdateUserPhotoUseCase,
    private val preparePhotoPartUseCase: PreparePhotoPartUseCase,
    private val createServiceUseCase: CreateServiceUseCase,
    private val deleteServiceUseCase: DeleteServiceUseCase,
    @ApplicationContext private val context: Context,
) : BaseViewModel<UserState, UserIntent, UserEffect>() {


    override val initialState = UserState()


    init{
        viewModelScope.launch {
            getUserDataUseCase().collect {user ->
                setState {
                    var state = this.copy(
                        user = user,
                        numberDraft = user?.phoneNumber ?: "",
                        cityDraft = user?.city ?: CitiesEnum.UNKNOWN,
                        emailDraft = user?.email ?: "",
                    )
                    if(user is Master){
                        state = state.copy(
                            descriptionDraft = user.description,
                            experienceDraft = user.experienceInYears.toString(),
                            specDraft = user.masterSpecialization
                        )
                    }
                    state
                }
            }
        }
    }
    init {
        viewModelScope.launch {
            _state.collect { it ->
                setState {
                    copy(
                        showSave = checkChanges(this)
                    )
                }

            }
        }
    }

    override fun handleIntent(intent: UserIntent) {
        when (intent) {

            is UserIntent.ChangeAvatar -> {
                sendEffect(OpenPhotoPicker(intent.typeOfSelect))

            }

            UserIntent.CloseSheet -> {
                setState {
                    copy(
                        avatarSheetState = false
                    )
                }
            }

            UserIntent.OpenSheet -> {
                setState {
                    copy(
                        avatarSheetState = true
                    )
                }
            }

            is UserIntent.GetPhotoFromMedia -> {
                setState {
                    copy(
                        pendingUri = intent.uri
                    )
                }
            }

            UserIntent.CancelPhoto -> {
                setState {
                    copy(
                        pendingUri = null
                    )
                }
            }

            is UserIntent.ConfirmPhoto -> {
                viewModelScope.launch {
                    val localUri = saveToInternalUseCase(intent.uri)
                    setState {
                        copy(
                            newAvatarData = localUri,
                            pendingUri = null,
                            isPhotoSaving = true
                        )
                    }
                    val user = _state.value.user
                    user?.let {
                        val photo = preparePhotoPartUseCase(context, intent.uri)
                        photo?.let {
                            withContext(NonCancellable) {
                                updateUserPhotoUseCase(user.id, photo)
                            }
                            setState {
                                copy(
                                    isPhotoSaving = false
                                )
                            }

                        }
                    }

                }
            }

            is UserIntent.CurrentMasterIntent.ChangeDescription -> {
                setState {
                    copy(
                        descriptionDraft = intent.description
                    )
                }

            }

            is UserIntent.CurrentMasterIntent.ChangeExperience -> {
                setState {
                    copy(
                        experienceDraft = intent.experience
                    )
                }

            }

            is UserIntent.CurrentMasterIntent.ChangeSpecialization -> {
                setState {
                    copy(
                        specDraft = intent.spec
                    )
                }


            }


            is UserIntent.ChangeNumber -> {
                setState {
                    copy(
                        numberDraft = intent.number
                    )
                }


            }

            is UserIntent.ChangeCity -> {
                setState {
                    copy(
                        cityDraft = intent.city
                    )
                }

            }

            is UserIntent.ChangeEmail -> {
                setState {
                    copy(
                        emailDraft = intent.email
                    )
                }
            }

            UserIntent.SaveChanges -> {
                viewModelScope.launch {
                    updateProfile()
                }

            }

            is UserIntent.CurrentMasterIntent.CreateService -> {
                viewModelScope.launch {
                    val state = _state.value
                    val user = state.user
                    if(user !is Master) return@launch
                    val dto = MasterServiceDTO(
                        id = null,
                        masterId = user.masterId,
                        service = state.serviceDraft,
                        price = state.priceDraft.toIntOrNull()?:0,
                        position = null
                    )
                   createServiceUseCase(dto)
                }

            }

            is UserIntent.CurrentMasterIntent.DeleteService -> {
                viewModelScope.launch {
                    deleteServiceUseCase(intent.id)
                }

            }
            is UserIntent.CurrentMasterIntent.UpdateService -> {
                viewModelScope.launch {
                    //потом добавлю
                }

            }

            UserIntent.CurrentMasterIntent.CloseCreate -> {
                setState {
                    copy(
                        showCreate = false
                    )
                }
            }
            UserIntent.CurrentMasterIntent.OpenCreate -> {
                setState {
                    copy(
                        showCreate = true
                    )
                }
            }

            is UserIntent.CurrentMasterIntent.ChangePriceDraft -> {
               setState {
                   copy(
                       priceDraft = intent.value
                   )
               }
            }
            is UserIntent.CurrentMasterIntent.ChangeServiceDraft -> {
                setState {
                    copy(
                        serviceDraft = intent.value
                    )
                }
            }
        }


    }



    private suspend fun updateProfile(){
        setState {
            copy(
                isSaving = true
            )
        }
        val state = _state.value

        val user = state.user?: return
        val finalUser = if (user is Master) {
            user.copy(
                phoneNumber = state.numberDraft,
                email = state.emailDraft,
                city = state.cityDraft,
                description = state.descriptionDraft,
                experienceInYears = state.experienceDraft.toIntOrNull() ?: user.experienceInYears,
                masterSpecialization = state.specDraft
            )
        } else {
            user.copy(
                phoneNumber = state.numberDraft,
                email = state.emailDraft,
                city = state.cityDraft
            )
        }

        updateUserDataUseCase(finalUser)
        setState {
            copy(
                isSaving = false
            )
        }

    }

    private fun checkChanges(state: UserState) : Boolean {
        val user = state.user ?: return false
        val userDataChanges = state.numberDraft.trim() != user.phoneNumber.trim() || state.cityDraft != user.city
        val masterDataChanges = if (user is Master) {
            state.specDraft != user.masterSpecialization ||
                    (state.experienceDraft.toIntOrNull() ?: 0) != user.experienceInYears ||
                    state.descriptionDraft != user.description

        } else {
            false
        }
        return userDataChanges || masterDataChanges
    }


}

