package com.didi.githubuser.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.didi.githubuser.R
import com.didi.githubuser.ViewModel.DetailUserViewModel
import com.didi.githubuser.databinding.ActivityDetailUserBinding
import com.didi.githubuser.helper.SectionsPagerAdapter
import com.didi.githubuser.helper.ZoomOutPageTransformer
import com.google.android.material.tabs.TabLayoutMediator
import androidx.core.content.ContextCompat
import com.bumptech.glide.request.RequestOptions
import com.didi.githubuser.MainActivity
import com.didi.githubuser.MainActivity.Companion.TEST
import com.didi.githubuser.ViewModel.FavoriteAddUpdateViewModel
import com.didi.githubuser.ViewModel.ViewModelFactory
import com.didi.githubuser.database.User
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var userAddUpdateViewModel: FavoriteAddUpdateViewModel
    private lateinit var detailUser: User
    lateinit var username: String
    private var isFavorite: Boolean = false

    companion object {
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

        val bundle: Bundle? = intent.extras
        username = bundle?.getString(MainActivity.USERNAME) as String

        userAddUpdateViewModel = obtainViewModel(this)

        detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailUserViewModel::class.java)
        setUser(username)

        detailUserViewModel.isLoading.observe(this, { isLoading ->
            showLoading(isLoading)
        })

        userAddUpdateViewModel.getAllUser().observe(this, { listUser ->
            setFavorite(false)
            listUser.forEach {
                if (it.login.equals(username)){
                    Log.d("test", "name: ${it.login}")
                    setFavorite(true)
                    setIcon(true)
                }
            }
            Log.d(TEST, "isFavorite in getAll: $isFavorite")
        })

        detailUserViewModel.detailUser.observe(this, { detailUserItem ->
            if (detailUserItem != null){
                showLoading(false)
                detailUser = User(login = detailUserItem.login,
                avatarUrl = detailUserItem.avatarUrl,
                url = detailUserItem.url, htmlUrl = detailUserItem.htmlUrl)

                val url = detailUserItem.avatarUrl
                binding.avatar.loadImage(url)

                val name = detailUserItem.name
                val location = detailUserItem.location

                val nameLocation = "$name, $location"
                binding.tvName.text = nameLocation
                binding.tvBio.text = detailUserItem.bio
                binding.tvCompany.text = detailUserItem.company
                binding.tvEmail.text = detailUserItem.email
                if (detailUserItem.bio != "null"){
                    binding.tvBio.text = detailUserItem.bio
                }else {
                    binding.tvBio.visibility = View.GONE
                }
                if (detailUserItem.company != "null"){
                    binding.tvCompany.text = detailUserItem.company
                }else {
                    binding.tvCompany.visibility = View.GONE
                }
                if (detailUserItem.email != "null"){
                    binding.tvEmail.text = detailUserItem.email
                }else {
                    binding.tvEmail.visibility = View.GONE
                }

                binding.tvJmlhFollowers.text = detailUserItem.followers.toString()
                binding.tvJmlhFollowing.text = detailUserItem.following.toString()
                binding.tvJmlhRepositories.text = detailUserItem.publicRepos.toString()
            }
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(this, username)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.setPageTransformer(ZoomOutPageTransformer())
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITTLES[position])
        }.attach()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        initCollapsingToolbar(username)
        binding.collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGrey))
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white))
        binding.fabAdd.setOnClickListener(this)

    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteAddUpdateViewModel::class.java)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.fab_add -> {
                Toast.makeText(this, "fab click", Toast.LENGTH_SHORT).show()
                if (!isFavorite){
                    userAddUpdateViewModel.insert(detailUser)
                    setIcon(true)
                }else {
                    Log.d("test", "delete: username $username")
                    userAddUpdateViewModel.deleteByUsername(username)
                    setIcon(false)
                }
            }
        }
    }

    private fun setFavorite(isFavorite: Boolean){
        this.isFavorite = isFavorite
    }

    private fun setIcon(isFavorite: Boolean){
        Log.d("test", "isFavorite in setIcon(): $isFavorite")
        if (isFavorite){
            binding.fabAdd.setImageResource(R.drawable.bg_search)
        }else {
            binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_24)
        }
    }

    private fun initCollapsingToolbar(title: String) {
        val collapsingToolbar = binding.collapsingToolbarLayout
        collapsingToolbar.title = " "
        val appBarLayout = binding.appBar
        appBarLayout.setExpanded(true)
        appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
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

    private fun setUser(username: String?){
        if (username != null){
            detailUserViewModel.setDetailUser(username)
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.shimmerDetailUser.visibility = View.VISIBLE
        }else {
            binding.shimmerDetailUser.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun ImageView.loadImage(url: String){
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(100, 100))
            .into(this)
    }
}