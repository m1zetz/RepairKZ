package com.example.repairkz.Activity

sealed class ActivityEffects{
    object NavigateToLogin : ActivityEffects()
    object NavigateToMainWindow : ActivityEffects()
}