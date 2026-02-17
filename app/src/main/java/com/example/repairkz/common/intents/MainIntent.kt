package com.example.repairkz.common.intents


interface AppIntent
sealed class MainIntent : AppIntent {
    data class ToUserScreen(val id: Int) : MainIntent()

    data class ToLastScreen(val id: Int) : MainIntent()
}