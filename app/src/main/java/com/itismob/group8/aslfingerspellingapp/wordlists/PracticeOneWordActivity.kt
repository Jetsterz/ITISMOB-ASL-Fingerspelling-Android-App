package com.itismob.group8.aslfingerspellingapp.wordlists

import android.R
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.mediapipe.tasks.components.containers.Category
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityPracticeOneWordBinding
import com.itismob.group8.aslfingerspellingapp.wordlists.adapters.ViewWordDemoAdapter
import com.itismob.group8.aslfingerspellingapp.libraries.Camera
import com.itismob.group8.aslfingerspellingapp.libraries.GestureRecognizerHelper
import com.itismob.group8.aslfingerspellingapp.wordlists.adapters.ViewWordDemoAdapter.ViewWordDemoViewHolder.Companion.mapOfSigns
import com.itismob.group8.aslfingerspellingapp.wordlists.database.DictioWordDatabase
import com.itismob.group8.aslfingerspellingapp.wordlists.database.UserWordDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.text.uppercaseChar

class PracticeOneWordActivity : AppCompatActivity(), GestureRecognizerHelper.GestureRecognizerListener {
    //practice logic
    private lateinit var b : ActivityPracticeOneWordBinding
    private lateinit var cam : Camera
    private lateinit var exec : ExecutorService
    private lateinit var wordToFill : Word
    private lateinit var gest : GestureRecognizerHelper
    private var currLetter = 0
    private var detectedLetter = ""
    private lateinit var checkWord: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        b = ActivityPracticeOneWordBinding.inflate(layoutInflater)
        setContentView(b.root)

        this.cam = Camera(
            this,
            b.pvCameraPreviewPractice
        )
        exec = Executors.newSingleThreadExecutor()
        exec.execute {
            gest = GestureRecognizerHelper(
                context = this,
                gestureRecognizerListener = this
            )
            runOnUiThread {
                cam.setGestureRecognizer(gest)
                if (cam.allPermissionsGranted()) {
                    cam.startCamera()
                } else {
                    cam.requestPermissions()
                }

                this.cam.setCaptureVideoButton(b.fabRecordPracticeWord)
            }
        }
        b.prompt.text = "Spell the word below in sign language!"
        b.fabHome.setOnClickListener {
            finish()
        }
        b.fabRecordPracticeWord.setOnClickListener {
            cam.captureVideo()
        }
        b.fabSwitchCam.setOnClickListener {
            cam.flipCamera()
        }
        b.ibHint2.setOnClickListener {
            showDialogwithIcon(this)
        }
        val i = intent
        val db = when(i.getStringExtra("list") ) {
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
        val wordCheck = db.findWordByID(i.getIntExtra("id", -1))
        if (wordCheck == null) {
            Toast.makeText(
                this,
                "ERROR: Word not found.",
                Toast.LENGTH_LONG
            ).show()
            finish()
            return
        }
        wordToFill = wordCheck
        changePracticeWord(wordToFill)
    }


    //TODO: Fix ts
    fun showDialogwithIcon(context: Context) {
        val builder = AlertDialog.Builder(context, com.itismob.group8.aslfingerspellingapp.R.style.AlertDialogTheme)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(com.itismob.group8.aslfingerspellingapp.R.layout.dialog_layout, null)

        val letter = wordToFill.wordName[currLetter].uppercaseChar()
        // Find the ImageView and set the icon
        val iconImage: ImageView = dialogView.findViewById(com.itismob.group8.aslfingerspellingapp.R.id.dialog_icon)
        val imageURI = mapOfSigns[letter]
        when (imageURI) {
            null -> iconImage.setImageResource(com.itismob.group8.aslfingerspellingapp.R.drawable.hand)
            else -> iconImage.setImageResource(imageURI)
        }

        val message: TextView = dialogView.findViewById(com.itismob.group8.aslfingerspellingapp.R.id.tv_message)
        message.text = "How to sign $letter"

        builder.setView(dialogView)
            .setTitle("Hint")

        builder.setNegativeButton("CLOSE") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(com.itismob.group8.aslfingerspellingapp.R.color.alertdialog_background)
        dialog.show()

        val negativeButton: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setTextColor(resources.getColor(com.itismob.group8.aslfingerspellingapp.R.color.alertdialog_buttoncolor)) // Set negative button text to red
    }

    private fun changePracticeWord(word: Word) {
        b.tvPracticeOneWord.text = word.wordName
        b.tvCategory.text = word.category
        updateCheckWord()
        currLetter = 0
    }
    private fun updateCheckWord() {
        this.checkWord = this.wordToFill.wordName.uppercase()
    }


    override fun onError(error: String, errorCode: Int) {
        Log.i("Error", "Error: $error")
    }

    private fun updateResults(categories: List<Category>?) {
        if (categories != null && categories.isNotEmpty()) {
            detectedLetter = categories[0].categoryName()
        } else detectedLetter = ""
    }
    private fun updateStringSpan(currentLetterIndex: Int) {
        val spanString = SpannableString(wordToFill.wordName)
        val greenText = ForegroundColorSpan(Color.GREEN)
        spanString.setSpan(greenText, 0, currentLetterIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        b.tvPracticeOneWord.text = spanString
    }

    override fun onResults(resultBundle: GestureRecognizerHelper.ResultBundle) {
        runOnUiThread {
            val gestureCategories = resultBundle.results.first().gestures()
            if (gestureCategories.isNotEmpty()) {
                updateResults(gestureCategories.first())

                if (currLetter < wordToFill.wordName.length && detectedLetter.isNotEmpty() && detectedLetter == this.checkWord[currLetter].toString()) {
                    currLetter++
                    updateStringSpan(currLetter)
                    if (currLetter < wordToFill.wordName.length) {
                        while (currLetter < wordToFill.wordName.length &&
                            !wordToFill.wordName[currLetter].isLetter() ) {
                            currLetter++
                        }
                    } else {
                        b.prompt.isVisible = false
                        b.tvPracticeOneWord.text = "Correct!"
                    }
                }
            } else {
                updateResults(emptyList())
            }
        }
    }
}