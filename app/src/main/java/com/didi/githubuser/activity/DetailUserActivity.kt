package com.didi.githubuser.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
import com.didi.githubuser.MainActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(true)

        val bundle: Bundle? = intent.extras
        var username = bundle?.getString(MainActivity.USERNAME)

        detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailUserViewModel::class.java)
        if (username != null) {
            detailUserViewModel.setDetailUser(username)
        }
        detailUserViewModel.getDetailUser().observe(this, { detailUserItem ->
            if (detailUserItem != null){
                showLoading(false)
                username = detailUserItem.login

//                binding.toolbarTitle.text = username
                val url = detailUserItem.avatarUrl
                val uri = Uri.parse(url)
                Glide.with(this)
                    .load(uri)
                    .into(binding.avatar)

                val name = detailUserItem.name ?: detailUserItem.login
                val location = detailUserItem.location ?: " "

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

                binding.tvJmlhFollowers.text = detailUserItem.follower.toString()
                binding.tvJmlhFollowing.text = detailUserItem.following.toString()
                binding.tvJmlhRepositories.text = detailUserItem.repository.toString()

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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        initCollapsingToolbar(username as String)
//        binding.collapsingToolbarLayout.title = username
        binding.collapsingToolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGrey))
        binding.collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white))
    }

    override fun onClick(v: View?) {
        when(v?.id){
//            R.id.back -> {
//                Toast.makeText(this, "click back", Toast.LENGTH_SHORT).show()
//                Intent(this, MainActivity::class.java).also { startActivity(it) }
//                finish()
//            }
//            R.id.toolbarTitle -> {
//                Toast.makeText(this, "click title", Toast.LENGTH_SHORT).show()
//
//            }
        }
    }

    private fun initCollapsingToolbar(tittle: String) {
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
                    collapsingToolbar.title = tittle
                    isShow = true
                } else if (isShow) {
                    collapsingToolbar.title = " "
                    isShow = false
                }
            }
        })
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

    override fun onBackPressed() {
        super.onBackPressed()
    }

    companion object {
        lateinit var username: String
        @StringRes
        private val TAB_TITTLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
}