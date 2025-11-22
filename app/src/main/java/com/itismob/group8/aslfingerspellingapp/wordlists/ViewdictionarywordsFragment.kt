package com.itismob.group8.aslfingerspellingapp.wordlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.itismob.group8.aslfingerspellingapp.wordlists.database.DictioWordDatabase
import com.itismob.group8.aslfingerspellingapp.R
import com.itismob.group8.aslfingerspellingapp.wordlists.Word
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
        val db = DictioWordDatabase(requireContext())
        val dat : ArrayList<Word> = db.getAllWords()
        lateinit var a : DictionaryWordsAdapter
        val showHideOnClickHandler = { pos: Int ->
            if (pos >= 0 && pos < dat.size) {
                val thisWord = dat[pos]
                db.flipShowHide(thisWord)
                val stateChange = !thisWord.showInPlay
                thisWord.showInPlay = stateChange
                a.notifyItemChanged(pos)

            }
        }
        a = DictionaryWordsAdapter(dat, showHideOnClickHandler)
        binding.dWordList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = a
        }
    }

}