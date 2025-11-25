package com.itismob.group8.aslfingerspellingapp.wordlists

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itismob.group8.aslfingerspellingapp.R
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityDisplayWordBinding
import com.itismob.group8.aslfingerspellingapp.wordlists.database.DictioWordDatabase
import com.itismob.group8.aslfingerspellingapp.wordlists.database.UserWordDatabase
import com.itismob.group8.aslfingerspellingapp.wordlists.database.WordDatabase

class DisplayWordActivity : AppCompatActivity() {
    private lateinit var b: ActivityDisplayWordBinding
    private lateinit var innerA : ViewWordDemoAdapter
    private lateinit var miniA : MiniDemoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDisplayWordBinding.inflate(layoutInflater)
        val v = b.root
        val i = intent
        val db : WordDatabase? = when(i.getStringExtra("list") ) {
            "UserWordDatabase" -> UserWordDatabase(this)
            "DictioWordDatabase" -> DictioWordDatabase(this)
            else -> null
        }
        if (db == null) {
            Toast.makeText(
                this,
                "ERROR: Database information not found.",
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }
        setContentView(v)
        val id = i.getIntExtra("id", -1)
        val w = db.findWordByID(id)!!
        val name = w.wordName
        val def = w.wordDef
        val cat = w.category
        val letters = name.toCharArray()
        b.viewWord.text = name
        b.viewDef.text = def
        b.viewCat.text = cat

        innerA = ViewWordDemoAdapter(letters)
        miniA = MiniDemoAdapter(letters)
        b.wordDemo.adapter = innerA
        b.minidemo.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )
        b.minidemo.adapter = miniA

        b.editCurrWord.setOnClickListener {
            val cats = db.getCategories()
            val dialogA = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                cats
            )

            val createDiaView = layoutInflater.inflate(R.layout.dialog_create_word, null)
            val nameIn = createDiaView.findViewById<EditText>(R.id.nameIn)
            val defIn = createDiaView.findViewById<EditText>(R.id.defIn)
            val catIn = createDiaView.findViewById<AutoCompleteTextView>(R.id.catIn)
            nameIn.setText(name)
            defIn.setText(def)
            catIn.setText(cat)

            catIn.setAdapter(dialogA)
            MaterialAlertDialogBuilder(this)
                    .setTitle("Edit Word")
                    .setView(createDiaView)
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Proceed") { _, _ ->
                        val newName = nameIn.text.toString()
                        val newDef = defIn.text.toString()
                        val newCat = catIn.text.toString()
                        db.updateWord(Word(w.id, newName, newDef, w.showInPlay, newCat))
                        finish()
                    }
                    .show()
            }

        b.delCurrWord.setOnClickListener {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete '${name}'?")

                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Delete") { _, _ ->
                        db.deleteWord(w)
                        Toast.makeText(this, "'${name}' was deleted.", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    .show()
            }
        }
    fun Int.toPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

}