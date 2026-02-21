package com.example.repairkz.ui.features.UserInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.domain.useCases.userData.GetProfileTypeUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserDataUseCase
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
    private val updateUserDataUseCase: UpdateUserDataUseCase
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

            }

            is UserIntent.MasterProfileIntent.DoOrder -> {

            }

            is UserIntent.MasterProfileIntent.Report -> {

            }

            is UserIntent.ChangeAvatar -> {
                viewModelScope.launch {
                    _channel.send(UserEffects.OpenPhotoPicker(intent.typeOfSelect))
                }

            }

            is UserIntent.SelectedPhoto -> {
                intent.uri?.let{ uri ->
                    viewModelScope.launch {
                        getUserDataUseCase().onSuccess {user->
                            val newUser = user.copy(
                                userPhotoUrl = uri.toString()
                            )
                            updateUserDataUseCase(newUser)
                            defineUser(comingId)
                        }


                    }



                }
            }

            UserIntent.CloseSheet -> {
                _uiState.update { currentState ->
                    if(currentState is UserState.Success){
                        currentState.copy(avatarSheetState = false)
                    }
                    else{
                        currentState
                    }
                }

            }
            UserIntent.OpenSheet -> {
                _uiState.update { currentState ->
                    if(currentState is UserState.Success){
                        currentState.copy(avatarSheetState = true)
                    }
                    else{
                        currentState
                    }
                }
            }
        }
    }

    init {
        defineUser(comingId)
    }
    fun defineUser(id:Int?){
        viewModelScope.launch {
            _uiState.value = UserState.Loading
            getProfileTypeUseCase(id).fold(
                onSuccess = { type ->
                    _uiState.value = UserState.Success(type)
                },
                onFailure = {
                    _uiState.value = UserState.Error(it.message.toString())
                }
            )
        }

    }
}

