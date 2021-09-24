package com.didi.githubuser.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.didi.githubuser.R
import com.didi.githubuser.ViewModel.FavoriteViewModel
import com.didi.githubuser.ViewModel.SettingViewModel
import com.didi.githubuser.ViewModel.ViewModelFactory
import com.didi.githubuser.ViewModel.ViewModelFactorySetting
import com.didi.githubuser.adapter.FavoriteAdapter
import com.didi.githubuser.databinding.ActivityFavoriteBinding
import com.didi.githubuser.helper.SettingPreferences
import com.google.android.material.appbar.AppBarLayout

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactorySetting(pref)).get(SettingViewModel::class.java)

        favoriteViewModel = obtainViewModel(this)
        favoriteViewModel.getAllUsers().observe(this, { listUsers ->
            Log.d("test", "size: ${listUsers.size}")
            if (listUsers != null){
                Log.d("test", "size listusers: ${listUsers.size}")
                adapter.setListUser(listUsers)
            }
        })

        adapter = FavoriteAdapter()
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        binding.rvFavorite.adapter = adapter

        setSupportActionBar(binding.toolbar)
//        val title = "Daftar user github \nfavorite"
        initCollapsingToolbar(resources.getString(R.string.daftar_user_github_favorite))
        binding.collapsingToolbarLayout.background = ContextCompat.getDrawable(this, R.drawable.bg_toolbar)
//        binding.collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGrey))
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white))

        settingViewModel.getNameSetting().observe(this, { name ->
            binding.tvName.text = name
        })
    }

    private fun initCollapsingToolbar(title: String) {
        val collapsingToolbar = binding.collapsingToolbarLayout
        collapsingToolbar.title = ""
        val appBar = binding.appBar
        appBar.setExpanded(true)
        appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.title = title
                    isShow = true
                } else if (isShow) {
                    collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }
}