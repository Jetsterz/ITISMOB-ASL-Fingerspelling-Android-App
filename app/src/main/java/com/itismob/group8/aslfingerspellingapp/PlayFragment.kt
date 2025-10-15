package com.itismob.group8.aslfingerspellingapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.itismob.group8.aslfingerspellingapp.databinding.FragmentPlayBinding


class PlayFragment : Fragment() {

    private var _viewBinding: FragmentPlayBinding?= null
    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentPlayBinding.inflate(inflater, container, false)

        viewBinding.btnCreateGame.setOnClickListener {
            val practiceIntent = Intent(requireActivity(), PracticeCategoryActivity::class.java)
            startActivity(practiceIntent)
        }

        viewBinding.btnViewGames.setOnClickListener {
            val translateIntent = Intent(requireActivity(), TranslateActivity::class.java)
            startActivity(translateIntent)
        }

        val view = viewBinding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}