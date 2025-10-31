package com.itismob.group8.aslfingerspellingapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.itismob.group8.aslfingerspellingapp.databinding.FragmentDictionaryBinding
import com.itismob.group8.aslfingerspellingapp.databinding.FragmentViewvideosBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DictionaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DictionaryFragment : Fragment() {
    private var _viewBinding: FragmentDictionaryBinding?= null
    private val viewBinding get() = _viewBinding!!
    private lateinit var tabLay: TabLayout
    private lateinit var pager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _viewBinding = FragmentDictionaryBinding.inflate(inflater, container, false)
        val view = viewBinding.root

        tabLay = view.findViewById(R.id.viewShifter)
        pager = view.findViewById(R.id.fragFill)

        pager.adapter = DictionaryPagerAdapter(this)

        TabLayoutMediator(tabLay, pager) {tab, pos ->
            tab.text = when (pos) {
                0 -> "View Dictionary"
                1 -> "View Custom Words"
                else -> null
            }
        }.attach()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}