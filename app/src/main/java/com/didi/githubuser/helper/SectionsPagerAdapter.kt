package com.didi.githubuser.helper

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.didi.githubuser.fragment.FollowFragment

class SectionsPagerAdapter(activity: AppCompatActivity, private val username: String ): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return FollowFragment.newInstance(position, username)
    }
}