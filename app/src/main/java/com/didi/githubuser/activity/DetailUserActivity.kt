package com.didi.githubuser.activity

import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.didi.githubuser.R
import com.didi.githubuser.ViewModel.DetailUserViewModel
import com.didi.githubuser.database.AppDatabase
import com.didi.githubuser.database.User
import com.didi.githubuser.databinding.ActivityDetailUserBinding
import com.didi.githubuser.helper.SectionsPagerAdapter
import com.didi.githubuser.helper.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator
import android.widget.Toast

import com.didi.githubuser.MainActivity

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
//    private lateinit var database: AppDatabase
    private lateinit var user: User

    companion object {
        val TAG = DetailUserActivity::class.java.simpleName
        lateinit var username: String
        @StringRes
        private val TAB_TITTLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = AppDatabase.createDatabase(this)

        val bundle: Bundle? = intent.extras
        var username = bundle?.getString("username")

        detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailUserViewModel::class.java)
        if (username != null) {
            detailUserViewModel.setDetailUser(username)
        }
        detailUserViewModel.getDetailUser().observe(this, { detailUserItem ->
            if (detailUserItem != null){
                Log.d("test", "berhasil mengambil data detail user")

                username = detailUserItem.login

                binding.collapsingToolbar.title = username

                val url = detailUserItem.avatar_url
                val uri = Uri.parse(url)
                Glide.with(this)
                    .load(uri)
                    .into(binding.avatar)

                binding.tvName.text = "${detailUserItem.name}, ${detailUserItem.location}"
                if (detailUserItem.bio != "null"){
                    binding.tvInfo.text = detailUserItem.bio
                }else {
                    binding.tvInfo.visibility = View.GONE
                }

                Log.d("test", detailUserItem.follower.toString())
                binding.tvJmlhFollowers.text = detailUserItem.follower.toString()
                binding.tvJmlhFollowing.text = detailUserItem.following.toString()
                binding.tvJmlhRepositories.text = detailUserItem.repository.toString()

                user = User(detailUserItem.login, detailUserItem.avatar_url, detailUserItem.github_url)
            }else {
                Log.d("test", "gagal mengambil data detail user")
            }
        })

        val sectionsPagerAdapter = username?.let { SectionsPagerAdapter(this, it) }
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.setPageTransformer(ZoomOutPageTransformer())
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITTLES[position])
        }.attach()

        binding.fab.setOnClickListener {
//            insertData(user)
            Log.d("test", "click fab")
//            db?.userDao()?.insertAll(user)
//            db?.userDao()?.getAll()?.forEach{
//                Log.d("test", "username: ${it.username}")
//            }
        }
    }

//    private fun insertData(user: User){
//        val thread = Thread{
//            database.userDao().insertAll(user)
//
//            database.userDao().getAll().forEach(){
//                Log.d("test", "username: ${it.username}")
//            }
//        }
//        thread.start()
//    }
}