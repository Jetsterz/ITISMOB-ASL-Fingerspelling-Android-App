package com.itismob.group8.aslfingerspellingapp.wordlists.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.itismob.group8.aslfingerspellingapp.wordlists.ViewdictionarywordsFragment
import com.itismob.group8.aslfingerspellingapp.wordlists.ViewuserwordsFragment

class DictionaryPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(pos: Int): Fragment {
        return when (pos) {
            0 -> ViewdictionarywordsFragment()
            1 -> ViewuserwordsFragment()
            else -> throw IllegalStateException("Position $pos Out of Bounds")
        }
    }
}