package com.example.repairkz.Activity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.Navigation.Routes
import com.example.repairkz.data.local.dataStore.DataStoreManager
import com.example.repairkz.data.registration.RegistrationRepositoryImpl
import com.example.repairkz.data.userData.UserRepository
import com.example.repairkz.domain.useCases.auth.RefreshTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val refreshToken: RefreshTokenUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ActivityState())
    val state = _state.asStateFlow()

    fun handleIntent(intent: ActivityIntent){
        when(intent){
            is ActivityIntent.ChangeTheme -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(isDarkTheme = intent.isDark)
                    }
                    dataStoreManager.saveThemeType(intent.isDark)
                }
            }
        }
    }

    init {
        tokenProcess()
        themeProcess()

    }

    fun themeProcess(){
        viewModelScope.launch {
            val isDark = dataStoreManager.isDarkThemeFlow.first()
            _state.update {
                it.copy(isDarkTheme = isDark)
            }
        }
    }

    fun tokenProcess(){
        viewModelScope.launch {
            val token = dataStoreManager.tokenFlow.first()
            if(token.isEmpty() || token == "-1"){
                _state.update {state ->
                    state.copy(
                        startDestination = StartDestination.Login
                    )
                }
            }
            else{
                val result = refreshToken()
                result.onSuccess {token ->
                    dataStoreManager.saveToken(token)
                    userRepository.getRoomData()
                    _state.update {state ->
                        state.copy(
                            startDestination = StartDestination.MainWindow
                        )
                    }
                }.onFailure {
                    _state.update {state ->
                        state.copy(
                            startDestination = StartDestination.Login
                        )
                    }
                }
            }
        }
    }


}