package com.didi.githubuser.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>){
    private val THEME_KEY = booleanPreferencesKey("theme_setting")
    private val NAME_KEY = stringPreferencesKey("name_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean){
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkModeActive
        }
    }

    fun getNameSetting(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[NAME_KEY] ?: "user"
        }
    }

    suspend fun saveNameSetting(name: String){
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = name
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences{
            return INSTANCE ?: synchronized(this){
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}