package com.didi.githubuser

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.didi.githubuser.ViewModel.SearchUserViewModel
import com.didi.githubuser.activity.DetailUserActivity
import com.didi.githubuser.adapter.SearchUserAdapter
import com.didi.githubuser.databinding.ActivityMainBinding
import com.didi.githubuser.model.ListUser

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchUserViewModel: SearchUserViewModel
    private lateinit var adapter: SearchUserAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SearchUserViewModel::class.java)

        searchUserViewModel.getSearchUser().observe(this, { searchUserItems ->
            val size = searchUserItems?.size
            if (searchUserItems != null && size != 0){
                adapter.setData(searchUserItems)
                showLoading(false)
                showRv(true)
            }else {
                showLoading(false)
                showRv(false)
                showNotFound(true)
            }
        })

        adapter = SearchUserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvSearchUser.layoutManager = LinearLayoutManager(this)
        binding.rvSearchUser.adapter = adapter

        adapter.setOnItemClickCallback(object : SearchUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ListUser) {
                val move = Intent(this@MainActivity, DetailUserActivity::class.java)
                move.putExtra("username", data.login)
                startActivity(move)
            }
        })

        adapter.setOnBtnGithubClickCallback(object : SearchUserAdapter.OnBtnGithubClickCallback{
            override fun onBtnGithubClickCallback(data: ListUser) {
                val url = data.html_url
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
                    searchUserViewModel.setSearchUser(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        binding.btnSetting.setOnClickListener(this)
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

    private fun showRv(state: Boolean){
        if (state){
            binding.rvSearchUser.visibility = View.VISIBLE
            binding.imgNotFound.visibility = View.GONE
            binding.progressbar.visibility = View.GONE
        }else {
            binding.rvSearchUser.visibility = View.GONE
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
            R.id.btn_setting -> {
                Intent(Settings.ACTION_LOCALE_SETTINGS).also { startActivity(it) }
            }
            R.id.tv_url_html -> {
                Toast.makeText(this, "click link gothub", Toast.LENGTH_SHORT).show()
            }
        }
    }
}