package com.itismob.group8.aslfingerspellingapp

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class DictionaryPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(pos: Int): Fragment {
        return when (pos) {
            0 -> ViewuserwordsFragment()
            1 -> ViewdictionarywordsFragment()
            else -> throw IllegalStateException("Position $pos Out of Bounds")
        }
    }
}