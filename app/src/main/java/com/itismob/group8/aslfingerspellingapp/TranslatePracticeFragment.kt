package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.itismob.group8.aslfingerspellingapp.databinding.FragmentPlayBinding
import com.itismob.group8.aslfingerspellingapp.databinding.FragmentTranslatepracticeBinding


class TranslatePracticeFragment : Fragment() {

    private var _viewBinding: FragmentTranslatepracticeBinding?= null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentTranslatepracticeBinding.inflate(inflater, container, false)
        val view = viewBinding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}