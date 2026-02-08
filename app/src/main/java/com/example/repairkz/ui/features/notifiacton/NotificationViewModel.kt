package com.example.repairkz.ui.features.notifiacton

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {

    private var _state = MutableStateFlow(NotificationState)
    val state = _state.asStateFlow()

}