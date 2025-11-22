package com.itismob.group8.aslfingerspellingapp.wordlists

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.itismob.group8.aslfingerspellingapp.R
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityDisplayWordBinding
import com.itismob.group8.aslfingerspellingapp.wordlists.database.DictioWordDatabase
import com.itismob.group8.aslfingerspellingapp.wordlists.database.UserWordDatabase
import com.itismob.group8.aslfingerspellingapp.wordlists.database.WordDatabase

class DisplayWordActivity : AppCompatActivity() {
    private lateinit var b: ActivityDisplayWordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDisplayWordBinding.inflate(layoutInflater)
        val v = b.root
        val i = intent
        val db : WordDatabase? = when(i.getStringExtra("list") ) {
            "UserWordDatabase" -> UserWordDatabase(this)
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
        val isVideo = i.getBooleanExtra("video?", false)
        val w = db.findWordByID(id)!!
        val name = w.wordName
        val def = w.wordDef
        val cat = w.category
        val link = w.videoLink
        b.viewWord.text = name
        b.viewDef.text = def
        b.viewCat.text = cat

        if (isVideo) {
            val s = "Now Playing: $link"
            b.viewVidStatus.text = s
            val path = "android.resource://$packageName/$link"
            b.wordVideo.setVideoPath(path)
            b.wordVideo.setOnPreparedListener { m ->
                m.setVolume(0f, 0f)
                m.isLooping = true
                b.wordVideo.start()
            }
        } else {
            b.wordVideo.isGone = true
        }
        b.editCurrWord.setOnClickListener {
            val createDiaView = layoutInflater.inflate(R.layout.dialog_create_word, null)
            createDiaView.findViewById<EditText>(R.id.nameIn).setHint(name)
            createDiaView.findViewById<EditText>(R.id.defIn).setHint(def)
            createDiaView.findViewById<AutoCompleteTextView>(R.id.catIn).setHint(cat)
            MaterialAlertDialogBuilder(this)
                .setTitle("Edit Word")
                .setView(createDiaView)
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Proceed") { _, _ ->
                    val newWord = createDiaView.findViewById<EditText>(R.id.nameIn).text.toString()
                    val newDef = createDiaView.findViewById<EditText>(R.id.defIn).text.toString()
                    val newCat = createDiaView.findViewById<AutoCompleteTextView>(R.id.catIn).text.toString()
                    db.updateWord(Word(id, newWord, newDef, null, true, newCat))
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Create Video?")
                        .setMessage("Would you like to add an associated ASL video? The current video will be removed otherwise.")
                        .setNegativeButton("Cancel") { vDialog, _ ->
                            vDialog.dismiss()
                            finish()
                        }
                        .setPositiveButton("Proceed to Video Creator") { _, _ ->
                            val newI = Intent(this, EditWordActivity::class.java)
                            newI.putExtra("id", id)
                            startActivity(newI)
                        }
                        .show()
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
}