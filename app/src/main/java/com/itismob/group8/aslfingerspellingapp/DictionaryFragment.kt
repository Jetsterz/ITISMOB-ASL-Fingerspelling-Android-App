package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _viewBinding = FragmentDictionaryBinding.inflate(inflater, container, false)
        val view = viewBinding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}