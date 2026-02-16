package com.example.repairkz.ui.features.UserInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.data.userData.UserRepository
import com.example.repairkz.domain.useCases.masterData.GetMasterByIdUseCase
import com.example.repairkz.domain.useCases.userData.GetProfileTypeUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProfileTypeUseCase: GetProfileTypeUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow<UserState>(UserState.Loading)
    val uiState = _uiState.asStateFlow()

    val comingId: Int? = try {
        savedStateHandle.get<Int>("userId")
    } catch (e: Exception) {
        savedStateHandle.get<String>("userId")?.toIntOrNull()
    }

    fun handleIntent(intent: UserIntent) {
        when (intent) {
            UserIntent.EditProfile -> {

            }

            is UserIntent.MasterProfileIntent.AddToFavorites -> {

            }

            is UserIntent.MasterProfileIntent.DoOrder -> {

            }

            is UserIntent.MasterProfileIntent.Report -> {

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

