package com.itismob.group8.aslfingerspellingapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.itismob.group8.aslfingerspellingapp.databinding.FragmentTranslatepracticeBinding
import com.itismob.group8.aslfingerspellingapp.practicetranslate.PracticeCategoryActivity
import com.itismob.group8.aslfingerspellingapp.practicetranslate.TranslateActivity

class TranslatePracticeFragment : Fragment() {

    private var _viewBinding: FragmentTranslatepracticeBinding?= null
    private val viewBinding get() = _viewBinding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentTranslatepracticeBinding.inflate(inflater, container, false)

        viewBinding.btnPractice.setOnClickListener {
            val practiceIntent = Intent(requireActivity(), PracticeCategoryActivity::class.java)
            startActivity(practiceIntent)
        }

        viewBinding.btnTranslate.setOnClickListener {
            val translateIntent = Intent(requireActivity(), TranslateActivity::class.java)
            startActivity(translateIntent)
        }

        viewBinding.ivInfo.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder
                .setMessage("Practice - Practice spelling a series of words in sign language with hints." +
                        "\n\nTranslate - Translates signed letters in real time.")
                .setTitle("Practice & Translate")
                .setNegativeButton("OK") {dialog, which ->

                }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        val view = viewBinding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}