package com.example.repairkz.ui.features.UserInfo

import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.domain.useCases.files.SaveToInternalUseCase
import com.example.repairkz.domain.useCases.userData.GetProfileTypeUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserDataUseCase
import com.example.repairkz.ui.features.UserInfo.UserEffects.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getProfileTypeUseCase: GetProfileTypeUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val saveToInternalUseCase: SaveToInternalUseCase,
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
                            updateUserDataUseCase(newUser)
                            defineUser(comingId)
                        }
                    }

                }
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
                    _uiState.value =
                        UserState.Success(type)
                },
                onFailure = {
                    _uiState.value = UserState.Error(it.message.toString())
                }
            )
        }

    }

}

