package com.example.repairkz.ui.features.UserInfo

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User
import com.example.repairkz.domain.useCases.files.PreparePhotoPartUseCase
import com.example.repairkz.domain.useCases.files.SaveToInternalUseCase
import com.example.repairkz.domain.useCases.userData.GetProfileTypeUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserPhotoUseCase
import com.example.repairkz.ui.features.UserInfo.UserEffects.*
import com.example.repairkz.ui.theme.darkAccept
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
    savedStateHandle: SavedStateHandle,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getProfileTypeUseCase: GetProfileTypeUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val saveToInternalUseCase: SaveToInternalUseCase,
    private val updateUserPhotoUseCase: UpdateUserPhotoUseCase,
    private val preparePhotoPartUseCase: PreparePhotoPartUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {


    private val _uiState = MutableStateFlow<UserState>(UserState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _channel = Channel<UserEffects>(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

    val comingId: Long? = try {
        savedStateHandle.get<Long>("userId")
    } catch (e: Exception) {
        savedStateHandle.get<String>("userId")?.toLongOrNull()
    }

    fun handleIntent(intent: UserIntent) {
        when (intent) {

            is UserIntent.MasterProfileIntent.AddToFavorites -> {
                Log.d("Сообщение", "Добавлен мастер в избранное с айди ${intent.masterId}")
            }

            is UserIntent.MasterProfileIntent.DoOrder -> {
                viewModelScope.launch {
                    _channel.send(NavigateToOrderRegistration(intent.masterId))
                }

            }

            is UserIntent.MasterProfileIntent.Report -> {
                Log.d("Сообщение", "Репорт на мастера с айди ${intent.masterId}")
            }

            is UserIntent.ChangeAvatar -> {
                viewModelScope.launch {
                    _channel.send(OpenPhotoPicker(intent.typeOfSelect))
                }

            }


            UserIntent.CloseSheet -> {
                _uiState.update { currentState ->
                    if (currentState is UserState.Success) {
                        currentState.copy(avatarSheetState = false)
                    } else {
                        currentState
                    }
                }
            }

            UserIntent.OpenSheet -> {
                _uiState.update { currentState ->
                    if (currentState is UserState.Success) {
                        currentState.copy(avatarSheetState = true)
                    } else {
                        currentState
                    }
                }
            }

            is UserIntent.GetPhotoFromMedia -> {
                _uiState.update { currentState ->
                    if (currentState is UserState.Success) {
                        currentState.copy(
                            pendingUri = intent.uri
                        )
                    } else {
                        currentState
                    }

                }
            }

            UserIntent.CancelPhoto -> {
                val currentState = _uiState.value
                if (currentState is UserState.Success) {
                    _uiState.value = currentState.copy(
                        pendingUri = null
                    )
                }
            }

            is UserIntent.ConfirmPhoto -> {
                viewModelScope.launch {
                    val localUri = saveToInternalUseCase(intent.uri)
                    val currentState = _uiState.value
                    if (currentState !is UserState.Success) return@launch
                    _uiState.value = currentState.copy(
                        newAvatarData = localUri,
                        pendingUri = null,
                        isPhotoSaving = true
                    )
                    val user = getUserDataUseCase()
                    user?.let {
                        val photo = preparePhotoPartUseCase(context, intent.uri)
                        photo?.let {
                            val result = withContext(NonCancellable) {
                                updateUserPhotoUseCase(user.id, photo)
                            }
                            result.onSuccess {
                                defineUser(comingId)
                            }
                            val newState = _uiState.value
                            if (newState !is UserState.Success) return@launch
                            _uiState.value = newState.copy(
                                isPhotoSaving = false
                            )
                        }
                    }

                }
            }

            is UserIntent.CurrentMasterIntent.ChangeDescription -> {
                val state = _uiState.value
                if (state is UserState.Success) {
                    _uiState.value = state.copy(
                        descriptionDraft = intent.description
                    )
                }

            }

            is UserIntent.CurrentMasterIntent.ChangeExperience -> {
                val state = _uiState.value
                if (state is UserState.Success) {
                    _uiState.value = state.copy(
                        experienceDraft = intent.experience
                    )
                }

            }

            is UserIntent.CurrentMasterIntent.ChangeSpecialization -> {
                val state = _uiState.value
                if (state is UserState.Success) {
                    _uiState.value = state.copy(
                        specDraft = intent.spec
                    )

                }


            }


            is UserIntent.ChangeNumber -> {
                val state = _uiState.value
                if (state is UserState.Success) {
                    _uiState.value = state.copy(
                        number = intent.number
                    )
                }


            }

            is UserIntent.ChangeCity -> {
                val state = _uiState.value
                if (state is UserState.Success) {
                    _uiState.value = state.copy(
                        city = intent.city
                    )
                }

            }

            is UserIntent.ChangeEmail -> {
                val state = _uiState.value
                if (state is UserState.Success) {
                    _uiState.value = state.copy(
                        email = intent.email
                    )
                }
            }

            UserIntent.SaveChanges -> {
                viewModelScope.launch {
                    val state = _uiState.value
                    if (state is UserState.Success) {
                        _uiState.value = state.copy(isSaving = true)
                        val userResult = updateProfile(state)
                        if (userResult.isSuccess) {
                            defineUser(comingId)
                        } else {
                            _uiState.value = state.copy(isSaving = false)
                        }

                    }
                }

            }
        }


    }



    private suspend fun updateProfile(state: UserState.Success): Result<Unit> {

        val type = state.userTypes
        if (type !is UserTypes.IsCurrentUser) return Result.failure(Exception("The state is not success"))
        val user = type.user

        val finalUser = if (type.isMaster && user is Master) {
            user.copy(
                phoneNumber = state.number,
                email = state.email,
                city = state.city,
                description = state.descriptionDraft ?: user.description,
                experienceInYears = state.experienceDraft?.toIntOrNull() ?: user.experienceInYears,
                masterSpecialization = state.specDraft ?: user.masterSpecialization
            )
        } else {
            user.copy(
                phoneNumber = state.number,
                email = state.email,
                city = state.city
            )
        }
        return updateUserDataUseCase(finalUser)
    }

    private fun checkChanges(state: UserState) : Boolean {
        if (state !is UserState.Success) return false
        val type = state.userTypes
        if (type !is UserTypes.IsCurrentUser) return false
        val user = type.user
        val userDataChanges = state.number.trim() != user.phoneNumber.trim() || state.city != user.city
        val masterDataChanges = if (user is Master) {
            state.specDraft != user.masterSpecialization ||
                    (state.experienceDraft.toIntOrNull() ?: 0) != user.experienceInYears ||
                    state.descriptionDraft != user.description
        } else {
            false
        }
        return userDataChanges || masterDataChanges
    }

    init {
        defineUser(comingId)
        viewModelScope.launch {
            uiState.collect {state ->
                if(state is UserState.Success){
                    val shouldShow = checkChanges(state)
                    if(state.showSave != shouldShow){
                        _uiState.value = state.copy(showSave = shouldShow)
                    }
                }

            }
        }
    }

    fun defineUser(id: Long?) {
        viewModelScope.launch {
            _uiState.value = UserState.Loading
            getProfileTypeUseCase(id).fold(
                onSuccess = { type ->
                    _uiState.value = type.toSuccessState()
                },
                onFailure = {
                    _uiState.value = UserState.Error(it.message.toString())
                }
            )
        }

    }

}

