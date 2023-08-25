package com.example.submission2.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.submission2.ui.follow.FollowFragment

class SectionPagerAdapter(
    activity: AppCompatActivity,
    private val username: String,
    private val callback: (String) -> Unit
) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return FollowFragment.newInstance(position, username, callback)
    }
}