package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.itismob.group8.aslfingerspellingapp.databinding.FragmentViewdictionarywordsBinding

class ViewdictionarywordsFragment : Fragment(R.layout.fragment_viewdictionarywords) {
    private var b: FragmentViewdictionarywordsBinding? = null
    private val binding get() = b!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentViewdictionarywordsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* The following is a PLACEHOLDER, and is meant to be deleted upon completion. */
        val placeDat : ArrayList<Word> = arrayListOf(
            Word("DictioPlace", "A placeholder for the Dictionary Word list.", "NoLink", "showing"),
            Word("DictioPlace2", "Another placeholder for the Dictionary Word List.", "NoLink", "hiding")
        )
        /* END OF PLACEHOLDER */
        val dat : ArrayList<Word> = placeDat //<- calls the placeholder
        val a : DictionaryWordsAdapter = DictionaryWordsAdapter(dat)

        binding.dWordList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = a
        }
    }

}