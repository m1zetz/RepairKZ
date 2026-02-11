package com.example.repairkz.ui.features.notifiacton

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.data.notificationData.NotificationRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

@HiltViewModel
class NotificationViewModel @Inject constructor(private val repository: NotificationRepository) : ViewModel() {

    private val _state = MutableStateFlow<NotificationState>(NotificationState.Loading)
    val state = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleIntent(intent: NotificationIntent){
        when(intent){
            is NotificationIntent.GetNotifications ->{
                viewModelScope.launch {
                    _state.value = NotificationState.Loading
                    try{
                        _state.value = NotificationState.Success(notifications = repository.getNotifications())
                    } catch(e: Exception){
                        _state.value = NotificationState.Error(message = "Ошибка запроса")
                    }
                }
            }
            is NotificationIntent.ShowDetails -> {
                val currentState = _state.value
                if(currentState is NotificationState.Success){
                    _state.value = currentState.copy(selectedOrder = intent.order)
                }

            }

            NotificationIntent.HideDetails -> {
                val currentState = _state.value
                if(currentState is NotificationState.Success){
                    _state.value = currentState.copy(selectedOrder = null)
                }
            }
        }
    }

}