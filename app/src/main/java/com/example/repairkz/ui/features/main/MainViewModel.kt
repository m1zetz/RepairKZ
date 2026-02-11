package com.example.repairkz.ui.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.data.userData.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {
    private val _screenIndexState = MutableStateFlow(MainUiState())
    val screenIndexState = _screenIndexState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.fetchUserData()
        }
    }
    fun handleIntent(intent: MainIntent){
        when(intent){
            is MainIntent.ChangeScreen -> {
                _screenIndexState.update {
                    it.copy(selectedIndex = intent.index)
                }
            }
        }
    }

}

