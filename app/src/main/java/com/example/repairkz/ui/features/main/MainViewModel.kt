package com.example.repairkz.ui.features.main

import androidx.lifecycle.ViewModel
import com.example.repairkz.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainState, MainIntent, MainEffect>() {

    override val initialState = MainState()

    override fun handleIntent(intent: MainIntent){
        when(intent){
            is MainIntent.ChangeScreen -> {
                setState {
                    copy(selectedIndex = intent.index)
                }
            }
        }
    }

}

