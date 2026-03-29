package com.example.repairkz.Activity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.data.local.dataStore.DataStoreManager
import com.example.repairkz.data.registration.RegistrationRepositoryImpl
import com.example.repairkz.data.userData.UserRepository
import com.example.repairkz.domain.useCases.auth.RefreshTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val refreshToken: RefreshTokenUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _channel = Channel<ActivityEffects>()
    val channel = _channel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val token = dataStoreManager.tokenFlow.first()
            if(token.isEmpty() || token == "-1"){
                _channel.send(ActivityEffects.NavigateToLogin)
            }
            else{
                val result = refreshToken()
                result.onSuccess {token ->
                    dataStoreManager.saveToken(token)
                    userRepository.getRoomData()
                    _channel.send(ActivityEffects.NavigateToMainWindow)
                }.onFailure {
                    Log.e("RefreshToken", "Failed: ${it.cause}")
                    _channel.send(ActivityEffects.NavigateToLogin)
                }
            }
        }

    }
}