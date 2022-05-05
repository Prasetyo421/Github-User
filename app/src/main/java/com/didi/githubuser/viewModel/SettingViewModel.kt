package com.didi.githubuser.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.didi.githubuser.helper.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun setThemeSetting(isDarkModeActive: Boolean){
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getNameSetting(): LiveData<String> {
        return pref.getNameSetting().asLiveData()
    }

    fun setNameSetting(name: String){
        viewModelScope.launch {
            pref.saveNameSetting(name)
        }
    }
}