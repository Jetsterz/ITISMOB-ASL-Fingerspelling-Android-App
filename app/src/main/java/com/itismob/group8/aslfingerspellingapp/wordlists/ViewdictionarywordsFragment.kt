package com.itismob.group8.aslfingerspellingapp.wordlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itismob.group8.aslfingerspellingapp.wordlists.database.DictioWordDatabase
import com.itismob.group8.aslfingerspellingapp.R
import com.itismob.group8.aslfingerspellingapp.wordlists.Word
import com.itismob.group8.aslfingerspellingapp.databinding.FragmentViewdictionarywordsBinding
import com.itismob.group8.aslfingerspellingapp.wordlists.adapters.DictionaryWordsAdapter
import com.itismob.group8.aslfingerspellingapp.wordlists.database.DatamuseDictioHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        var dat : ArrayList<Word> = db.getAllWords()
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
        val co = DatamuseDictioHandler(requireContext())
        lifecycleScope.launch {
            co.grepWords("Animals")
            co.grepWords("Food")
            co.grepWords("Objects")
            co.grepWords("Names")
            withContext(Dispatchers.Main) {
                a.notifyDataSetChanged()
            }
        }
        val cats = db.getCategories()
        val dialogA = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            cats
        )


        binding.search.setOnClickListener {
            val searchDiaView = layoutInflater.inflate(R.layout.dialog_search, null)
            val nameIn = searchDiaView.findViewById<EditText>(R.id.nameIn)
            val catIn = searchDiaView.findViewById<AutoCompleteTextView>(R.id.catIn)
            catIn.setAdapter(dialogA)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Search by Category")
                .setView(searchDiaView)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Proceed") { _, _ ->
                    var name : String? = null
                    var cat : String? = null
                    if (!nameIn.text.toString().isEmpty()) {
                        name = nameIn.text.toString()
                    }
                    if (!catIn.text.toString().isEmpty()){
                        cat = catIn.text.toString()
                    }
                    dat.clear()
                    dat.addAll(db.findWordByNameAndCat(name, cat))
                    a.notifyDataSetChanged()
                }
                .show()
        }
        binding.refresh.setOnClickListener { v ->
            dat.clear()
            dat.addAll(db.getAllWords())
            a.notifyDataSetChanged()
        }
        a = DictionaryWordsAdapter(dat, showHideOnClickHandler)
        binding.dWordList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = a
        }
    }

}