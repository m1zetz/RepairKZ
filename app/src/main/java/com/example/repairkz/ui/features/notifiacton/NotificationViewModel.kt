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

    private var _state = MutableStateFlow(NotificationState())
    val state = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleIntent(intent: NotificationIntent){
        when(intent){
            is NotificationIntent.GetNotifications ->{
                viewModelScope.launch {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                    try{
                        _state.update {
                            it.copy(notifications = repository.getNotifications(), isLoading = false)
                        }
                    } catch(e: Exception){
                        _state.update {
                            it.copy(error = "Ошибка запроса", isLoading = false)
                        }
                    }
                }
            }
            is NotificationIntent.ShowDetails -> {
                _state.update {
                    it.copy(selectedOrder = intent.order)
                }
            }

            NotificationIntent.HideDetails -> {
                _state.update {
                    it.copy(selectedOrder = null)
                }
            }
        }
    }

}