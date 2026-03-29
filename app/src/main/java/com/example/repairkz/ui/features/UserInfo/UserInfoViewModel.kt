package com.example.repairkz.ui.features.UserInfo

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.models.Master
import com.example.repairkz.domain.useCases.files.PreparePhotoPartUseCase
import com.example.repairkz.domain.useCases.files.SaveToInternalUseCase
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
    savedStateHandle: SavedStateHandle,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getProfileTypeUseCase: GetProfileTypeUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val saveToInternalUseCase: SaveToInternalUseCase,
    private val updateUserPhotoUseCase: UpdateUserPhotoUseCase,
    private val preparePhotoPartUseCase: PreparePhotoPartUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _uiState = MutableStateFlow<UserState>(UserState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _channel = Channel<UserEffects>(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

    val comingId: Int? = try {
        savedStateHandle.get<Int>("userId")
    } catch (e: Exception) {
        savedStateHandle.get<String>("userId")?.toIntOrNull()
    }

    fun handleIntent(intent: UserIntent) {
        when (intent) {

            is UserIntent.MasterProfileIntent.AddToFavorites -> {
                Log.d("Сообщение", "Добавлен мастер в избранное с айди ${intent.masterId}")
            }

            is UserIntent.MasterProfileIntent.DoOrder -> {
                Log.d("Сообщение", "Заказ мастера с айди ${intent.masterId}")
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
                if (currentState is UserState.Success){
                    _uiState.value = currentState.copy(
                        pendingUri = null
                    )
                }
            }
            is UserIntent.ConfirmPhoto -> {
                viewModelScope.launch {
                    val localUri = saveToInternalUseCase(intent.uri)
                    val currentState = _uiState.value
                    if (currentState is UserState.Success){
                        _uiState.value = currentState.copy(
                            newAvatarData = localUri,
                            pendingUri = null
                        )
                        val user = getUserDataUseCase()
                        user?.let {
                            val newUser = user.copy(
                                userPhotoUrl = localUri.toString()
                            )

                            val photo = preparePhotoPartUseCase(context, intent.uri)
                            photo?.let {
                                withContext(NonCancellable) {
                                    val result = updateUserPhotoUseCase(user.id.toLong(), photo)
                                }

                            }
                        }
                    }

                }
            }

            is UserIntent.CurrentMasterIntent.ChangeDescription -> {
                viewModelScope.launch {
                    val state = _uiState.value
                    if(state is UserState.Success){
                        _uiState.value = state.copy(
                            descriptionDraft = intent.description
                        )
//                        updateUserDataUseCase(master.copy(description = intent.description))
//                        defineUser(comingId)

                    }
                }


            }
            is UserIntent.CurrentMasterIntent.ChangeExperience -> {

            }
            is UserIntent.CurrentMasterIntent.ChangeSpecialization -> {

            }

            UserIntent.CurrentMasterIntent.SaveDescription -> {
                viewModelScope.launch {
                    val state = _uiState.value
                    if(state is UserState.Success){
                        val user = state.userTypes
                        if(user is UserTypes.IsCurrentMaster){
                            updateUserDataUseCase(user.master.copy(description = state.descriptionDraft))
                            defineUser(comingId)
                        }
                    }
                }


            }
            UserIntent.CurrentMasterIntent.SaveExperience -> {

            }
            UserIntent.CurrentMasterIntent.SaveSpecialization -> {

            }
        }


    }


    init {
        defineUser(comingId)
    }

    fun defineUser(id: Int?) {
        viewModelScope.launch {
            _uiState.value = UserState.Loading
            getProfileTypeUseCase(id).fold(
                onSuccess = { type ->
                    val initialDesc = if(type is UserTypes.IsCurrentMaster) type.master.description else ""
                    _uiState.value =
                        UserState.Success(type, descriptionDraft = initialDesc)

                },
                onFailure = {
                    _uiState.value = UserState.Error(it.message.toString())
                }
            )
        }

    }

}

