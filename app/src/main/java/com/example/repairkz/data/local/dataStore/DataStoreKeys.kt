package com.example.repairkz.data.local.dataStore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    val IS_DARK_THEME_KEY = booleanPreferencesKey("isDarkTheme")
}