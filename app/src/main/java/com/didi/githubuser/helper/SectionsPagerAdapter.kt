package com.didi.githubuser.helper

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.didi.githubuser.fragment.FollowFragment

class SectionsPagerAdapter(activity: AppCompatActivity, username: String ): FragmentStateAdapter(activity) {
    private val name = username
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        Log.d("section", name)
        return FollowFragment.newInstance(position, name)
    }
}