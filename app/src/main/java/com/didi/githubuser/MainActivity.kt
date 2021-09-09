package com.didi.githubuser

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.didi.githubuser.ViewModel.SearchUserViewModel
import com.didi.githubuser.activity.DetailUserActivity
import com.didi.githubuser.adapter.SearchUserAdapter
import com.didi.githubuser.databinding.ActivityMainBinding
import com.didi.githubuser.fragment.HomeFragment
import com.didi.githubuser.model.SearchUser

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
            if (searchUserItems != null){
                searchUserItems[0].login?.let { Log.d("test", it) }
                adapter.setData(searchUserItems)
                showLoading(false)
            }else {
                showLoading(false)
            }
        })

        adapter = SearchUserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvSearchUser?.layoutManager = LinearLayoutManager(this)
        binding.rvSearchUser?.adapter = adapter

        adapter.setOnItemClickCallback(object : SearchUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: SearchUser) {
                val move = Intent(this@MainActivity, DetailUserActivity::class.java)
                move.putExtra("username", data.login)
                startActivity(move)
            }

        })

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.searchUsers?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        binding.searchUsers?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
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

        binding.btnSetting?.setOnClickListener(this)
    }

    private fun showSelectedUser(data: SearchUser){
        Toast.makeText(this, "choose: ${data.login}", Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressbar?.visibility = View.VISIBLE
            binding.rvSearchUser?.visibility = View.GONE
        }else {
            binding.progressbar?.visibility = View.GONE
            binding.rvSearchUser?.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_setting -> {
                Toast.makeText(this, "setting language", Toast.LENGTH_SHORT).show()
            }

        }
    }
}