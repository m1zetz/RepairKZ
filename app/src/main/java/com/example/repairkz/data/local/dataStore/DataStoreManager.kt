package com.example.repairkz.data.local.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val PREFERENCES_NAME = "repair_references"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_NAME)

class DataStoreManager(
    private val context: Context
) {
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.AUTH_TOKEN_KEY] = token
        }
    }

    suspend fun clearDataStore(){
        context.dataStore.edit {
            it.clear()
        }
    }
    suspend fun saveThemeType(isDark: Boolean){
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME_KEY] = isDark
        }
    }
    val tokenFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.AUTH_TOKEN_KEY] ?: "-1"
        }
    val isDarkThemeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME_KEY] ?: true
        }
}
