package com.example.repairkz.Activity


data class ActivityState(
    val startDestination: StartDestination? = null,
    val isDarkTheme: Boolean = true
)

sealed class ActivityIntent{
    data class ChangeTheme(val isDark: Boolean) : ActivityIntent()
}
sealed class StartDestination{
    object Login : StartDestination()
    object MainWindow : StartDestination()
}