package com.example.repairkz.ui.features.home

import androidx.lifecycle.viewModelScope
import com.example.repairkz.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeState, HomeIntent, HomeEffect>() {
    override val initialState = HomeState

    override fun handleIntent(intent: HomeIntent){
        when(intent){
            is HomeIntent.ClickOnCard -> {
                viewModelScope.launch {
                    sendEffect(HomeEffect.NavigateToSearch(intent.pattern?.resID))
                }
            }
        }
    }
}

