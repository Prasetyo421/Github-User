package com.didi.githubuser.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import com.didi.githubuser.viewModel.SettingViewModel
import com.didi.githubuser.databinding.ActivitySettingBinding
import com.didi.githubuser.helper.SettingPreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.didi.githubuser.viewModel.ViewModelFactorySetting

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private lateinit var settingViewModel: SettingViewModel
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, ViewModelFactorySetting(pref)).get(
            SettingViewModel::class.java
        )

        var name: String
        settingViewModel.getNameSetting().observe(this, {
            if (it != null){
                name = it
            }
        })

        settingViewModel.getThemeSetting().observe(this, { isDarkMode ->
            if (isDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        })

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.setThemeSetting(isChecked)
        }

        binding.tvSettingLanguage.setOnClickListener {
            Intent(Settings.ACTION_LOCALE_SETTINGS).also { startActivity(it) }
        }

        binding.btnSettingName.setOnClickListener {
            name = binding.edtName.text.toString().trim()
            settingViewModel.setNameSetting(name)
            Toast.makeText(this, "berhasil merubah nama: $name", Toast.LENGTH_SHORT).show()
        }
    }
}