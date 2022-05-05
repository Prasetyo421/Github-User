package com.didi.githubuser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.didi.githubuser.viewModel.SearchUserViewModel
import com.didi.githubuser.viewModel.SettingViewModel
import com.didi.githubuser.viewModel.ViewModelFactorySetting
import com.didi.githubuser.activity.DetailUserActivity
import com.didi.githubuser.activity.FavoriteActivity
import com.didi.githubuser.activity.SettingActivity
import com.didi.githubuser.adapter.SearchUserAdapter
import com.didi.githubuser.databinding.ActivityMainBinding
import com.didi.githubuser.helper.SettingPreferences
import com.didi.githubuser.model.ItemsItem

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchUserViewModel: SearchUserViewModel
    private lateinit var adapter: SearchUserAdapter
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

    companion object {
        const val USERNAME = "username"
        const val TEST = "test"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showNotFound(true)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactorySetting(pref)).get(SettingViewModel::class.java)
        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        searchUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchUserViewModel::class.java)

        searchUserViewModel.listUsers.observe(this) { itemsItem ->
            if (itemsItem.isEmpty()) {
                showNotFound(true)
            } else {
                showNotFound(false)
                adapter.setData(ArrayList(itemsItem))
            }
        }

        searchUserViewModel.isLoading.observe(this) { state ->
            showLoading(state)
        }

        adapter = SearchUserAdapter()

        binding.rvSearchUser.layoutManager = LinearLayoutManager(this)
        binding.rvSearchUser.adapter = adapter

        adapter.setOnItemClickCallback(object : SearchUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem) {
                val move = Intent(this@MainActivity, DetailUserActivity::class.java)
                move.putExtra(USERNAME, data.login)
                startActivity(move)
            }
        })

        adapter.setOnBtnGithubClickCallback(object : SearchUserAdapter.OnBtnGithubClickCallback{
            override fun onBtnGithubClickCallback(data: ItemsItem) {
                val url = data.htmlUrl
                val move = Intent(ACTION_VIEW)
                move.data = Uri.parse(url)
                startActivity(move)
            }

        })

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchUsers.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.searchUsers.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query?.isEmpty() == false){
                    showLoading(true)
                    searchUserViewModel.searchUsers(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        setSupportActionBar(binding.toolbar)

    }

    private fun showNotFound(state: Boolean){
        if (state){
            binding.imgNotFound.visibility = View.VISIBLE
            binding.progressbar.visibility = View.GONE
            binding.rvSearchUser.visibility = View.GONE
        }else {
            binding.imgNotFound.visibility = View.GONE
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressbar.visibility = View.VISIBLE
            binding.rvSearchUser.visibility = View.GONE
            binding.imgNotFound.visibility = View.GONE
        }else {
            binding.progressbar.visibility = View.GONE
            binding.rvSearchUser.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_url_html -> {
                Toast.makeText(this, "click link github", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_setting, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Toast.makeText(this, "click item", Toast.LENGTH_SHORT).show()
        when(item.itemId){
            R.id.favorite -> {
                Toast.makeText(this, "click favorite", Toast.LENGTH_SHORT).show()
                Intent(this, FavoriteActivity::class.java).also { startActivity(it) }
            }
            R.id.setting -> {
                Toast.makeText(this, "click setting", Toast.LENGTH_SHORT).show()
                Intent(this, SettingActivity::class.java).also { startActivity(it) }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}