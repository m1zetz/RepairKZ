package com.example.repairkz.ui.features.UserInfo

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.models.Master
import com.example.repairkz.data.remote.dto.MasterServiceDTO
import com.example.repairkz.domain.useCases.files.PreparePhotoPartUseCase
import com.example.repairkz.domain.useCases.files.SaveToInternalUseCase
import com.example.repairkz.domain.useCases.services.CreateServiceUseCase
import com.example.repairkz.domain.useCases.services.UpdateServiceUseCase
import com.example.repairkz.domain.useCases.services.DeleteServiceUseCase
import com.example.repairkz.domain.useCases.services.GetServicesUseCase
import com.example.repairkz.domain.useCases.userData.GetProfileTypeUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserPhotoUseCase
import com.example.repairkz.ui.features.UserInfo.UserEffects.*
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

    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val saveToInternalUseCase: SaveToInternalUseCase,
    private val updateUserPhotoUseCase: UpdateUserPhotoUseCase,
    private val preparePhotoPartUseCase: PreparePhotoPartUseCase,
    private val createServiceUseCase: CreateServiceUseCase,
    private val deleteServiceUseCase: DeleteServiceUseCase,
    private val updateServiceUseCase: UpdateServiceUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {


    private val _uiState = MutableStateFlow<UserState>(UserState())
    val uiState = _uiState.asStateFlow()

    private val _channel = Channel<UserEffects>(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

    fun handleIntent(intent: UserIntent) {
        when (intent) {

            is UserIntent.ChangeAvatar -> {
                viewModelScope.launch {
                    _channel.send(OpenPhotoPicker(intent.typeOfSelect))
                }

            }

            UserIntent.CloseSheet -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        avatarSheetState = false
                    )
                }
            }

            UserIntent.OpenSheet -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        avatarSheetState = true
                    )
                }
            }

            is UserIntent.GetPhotoFromMedia -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        pendingUri = intent.uri
                    )
                }
            }

            UserIntent.CancelPhoto -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        pendingUri = null
                    )
                }
            }

            is UserIntent.ConfirmPhoto -> {
                viewModelScope.launch {
                    val localUri = saveToInternalUseCase(intent.uri)
                    _uiState.update { currentState ->
                        currentState.copy(
                            newAvatarData = localUri,
                            pendingUri = null,
                            isPhotoSaving = true
                        )
                    }
                    val user = _uiState.value.user
                    user?.let {
                        val photo = preparePhotoPartUseCase(context, intent.uri)
                        photo?.let {
                            withContext(NonCancellable) {
                                updateUserPhotoUseCase(user.id, photo)
                            }
                            _uiState.update { currentState ->
                                currentState.copy(
                                    isPhotoSaving = false
                                )
                            }

                        }
                    }

                }
            }

            is UserIntent.CurrentMasterIntent.ChangeDescription -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        descriptionDraft = intent.description
                    )
                }

            }

            is UserIntent.CurrentMasterIntent.ChangeExperience -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        experienceDraft = intent.experience
                    )
                }

            }

            is UserIntent.CurrentMasterIntent.ChangeSpecialization -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        specDraft = intent.spec
                    )
                }


            }


            is UserIntent.ChangeNumber -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        numberDraft = intent.number
                    )
                }


            }

            is UserIntent.ChangeCity -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        cityDraft = intent.city
                    )
                }

            }

            is UserIntent.ChangeEmail -> {
                _uiState.update { currentState ->
                    currentState.copy(
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
                    val state = _uiState.value
                    val user = _uiState.value.user
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
                _uiState.update { currentState ->
                    currentState.copy(
                        showCreate = false
                    )
                }
            }
            UserIntent.CurrentMasterIntent.OpenCreate -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        showCreate = true
                    )
                }
            }

            is UserIntent.CurrentMasterIntent.ChangePriceDraft -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        priceDraft = intent.value
                    )
                }
            }
            is UserIntent.CurrentMasterIntent.ChangeServiceDraft -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        serviceDraft = intent.value
                    )
                }
            }
        }


    }



    private suspend fun updateProfile(): Result<Unit> {
        val state = _uiState.value
        val user = state.user?:return Result.failure(Exception("current user is null"))
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
        return updateUserDataUseCase(finalUser)
    }

    private fun checkChanges(state: UserState) : Boolean {
        val user = state.user
        if (user == null) return false
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

