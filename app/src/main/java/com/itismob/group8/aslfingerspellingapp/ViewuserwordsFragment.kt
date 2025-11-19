package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = UserWordDatabase(requireContext())
        val dat : ArrayList<Word> = db.getAllWords()
        lateinit var a : UserWordsAdapter

        binding.addButton.setOnClickListener {
            Toast.makeText(requireContext(), "I would show Create Word UI... IF I HAD ANY!", Toast.LENGTH_SHORT).show()
            //TODO("Actually give the guy a Create Word activity.")
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
                    .setPositiveButton("Delete") { dialog, _ ->
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
}