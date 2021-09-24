package com.didi.githubuser.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.didi.githubuser.helper.SettingPreferences
import java.lang.IllegalArgumentException

class ViewModelFactorySetting(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)){
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class ${modelClass.name}")
    }
}