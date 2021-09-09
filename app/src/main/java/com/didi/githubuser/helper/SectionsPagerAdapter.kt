package com.didi.githubuser.helper

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.didi.githubuser.fragment.FollowersFragment
import com.didi.githubuser.fragment.FollowingFragment
import com.didi.githubuser.fragment.HomeFragment

class SectionsPagerAdapter(activity: AppCompatActivity, username: String ): FragmentStateAdapter(activity) {
    private val name = username
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        Log.d("section", name)
        return HomeFragment.newInstance(position, name)
    }
}