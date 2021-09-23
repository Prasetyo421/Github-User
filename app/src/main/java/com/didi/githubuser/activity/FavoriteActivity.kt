package com.didi.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.didi.githubuser.ViewModel.FavoriteViewModel
import com.didi.githubuser.ViewModel.ViewModelFactory
import com.didi.githubuser.adapter.FavoriteAdapter
import com.didi.githubuser.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }
}