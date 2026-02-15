package com.example.repairkz.ui.features.UserInfo

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.models.Master
import com.example.repairkz.common.models.User
import com.example.repairkz.data.userData.UserRepository
import com.example.repairkz.domain.useCases.masterData.GetMasterByIdUseCase
import com.example.repairkz.domain.useCases.masterData.GetMastersUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getMasterByIdUseCase: GetMasterByIdUseCase,
    private val userRepository: UserRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow<UserState>(UserState.Loading)
    val uiState = _uiState.asStateFlow()

    val currentId: Int? = try {
        savedStateHandle.get<Int>("userId")
    } catch (e: Exception) {
        savedStateHandle.get<String>("userId")?.toIntOrNull()
    }

    fun handleIntent(intent: UserIntent){
        when(intent){
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
        observeUser()
    }

    fun observeUser(){
        viewModelScope.launch {
            val result = getUserDataUseCase()
            result.onSuccess { user ->
                val currentState = _uiState.value
                if(currentState is UserState.Success){
                    _uiState.value = currentState.copy(
                        clientId = user.userId
                    )
                }
            }
            result.onFailure {
                _uiState.value = UserState.Error("Ошибка получения id")
            }

        }
    }
    private fun loadUser() {
        viewModelScope.launch {
            val userResult = getUserDataUseCase()
            userResult.onSuccess { user ->
                if (user.userId == currentId || currentId == null) {
                    _uiState.value = UserState.Success(user)
                } else{
                    loadMaster(currentId)
                }
            }
        }
    }
    suspend fun loadMaster(id: Int){
        val masterResult = getMasterByIdUseCase(id)
        masterResult.onSuccess { master ->
            _uiState.value = UserState.Success(master)

        }
        masterResult.onFailure {
            _uiState.value = UserState.Error("Ошибка сети")

        }
    }

    init {
        loadUser()
    }
}

