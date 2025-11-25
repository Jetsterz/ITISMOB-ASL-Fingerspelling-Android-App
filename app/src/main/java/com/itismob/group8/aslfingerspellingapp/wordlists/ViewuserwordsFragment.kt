package com.itismob.group8.aslfingerspellingapp.wordlists

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itismob.group8.aslfingerspellingapp.R
import com.itismob.group8.aslfingerspellingapp.wordlists.database.UserWordDatabase
import com.itismob.group8.aslfingerspellingapp.wordlists.Word
import com.itismob.group8.aslfingerspellingapp.databinding.FragmentViewuserwordsBinding

class ViewuserwordsFragment : Fragment(R.layout.fragment_viewuserwords) {
    private var b: FragmentViewuserwordsBinding? = null
    private val binding get() = b!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        b = FragmentViewuserwordsBinding.inflate(inflater, container, false)
        return binding.root
    }
    private lateinit var a : UserWordsAdapter
    private lateinit var db : UserWordDatabase
    private lateinit var dat : ArrayList<Word>

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = UserWordDatabase(requireContext())
        dat = db.getAllWords()
        val cats = db.getCategories()
        val dialogA = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            cats
        )

        val createDiaView = layoutInflater.inflate(R.layout.dialog_create_word, null)
        val nameIn = createDiaView.findViewById<EditText>(R.id.nameIn)
        val defIn = createDiaView.findViewById<EditText>(R.id.defIn)
        val catIn = createDiaView.findViewById<AutoCompleteTextView>(R.id.catIn)
        catIn.setAdapter(dialogA)

        binding.addButton.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Create New Word")
                .setView(createDiaView)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Proceed") { _, _ ->
                    val name = nameIn.text.toString()
                    val def = defIn.text.toString()
                    val cat = catIn.text.toString()
                    val id = db.addWord(Word(-1, name, def, true, cat))
                    dat.add(db.findWordByID(id)!!)
                    val pos = dat.size - 1
                    a.notifyItemInserted(pos)
                }
                    .show()
        }

        val showHideOnClickHandler = { pos: Int ->
            if (pos >= 0 && pos < dat.size) {
                val thisWord = dat[pos]
                db.flipShowHide(thisWord)
                val stateChange = !thisWord.showInPlay
                thisWord.showInPlay = stateChange
                a.notifyItemChanged(pos)
            }
        }
        val deleteOnClickHandler = { pos: Int ->
            if (pos >= 0 && pos < dat.size) {
                val thisWordName = dat[pos].wordName
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete '${dat[pos].wordName}'?")

                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Delete") { _, _ ->
                        db.deleteWord(dat[pos])
                        dat.removeAt(pos)
                        a.notifyItemRemoved(pos)
                        Toast.makeText(requireContext(), "'${thisWordName}' was deleted.", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }
        }

        a = UserWordsAdapter(dat, showHideOnClickHandler, deleteOnClickHandler)
        binding.uWordList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = a
        }
    }
    override fun onResume() {
        super.onResume()
        dat.clear()
        dat.addAll(db.getAllWords())
        a.notifyDataSetChanged()
    }
}