package com.itismob.group8.aslfingerspellingapp

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityPracticeCameraBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.google.mediapipe.tasks.components.containers.Category
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds

class PracticeCameraActivity : AppCompatActivity(), GestureRecognizerHelper.GestureRecognizerListener {
    private lateinit var viewBinding: ActivityPracticeCameraBinding
    private lateinit var camera: Camera
    private lateinit var backgroundExecutor: ExecutorService
    private lateinit var gestureRecognizerHelper: GestureRecognizerHelper
    private var practiceWord = "Hello World"
    private var currLetter = 0
    private lateinit var checkWord: String
    private var nameCategories: MutableList<Category?> = mutableListOf()

    companion object {
        const val CATEGORY_KEY = "CATEGORY_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewBinding = ActivityPracticeCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //recycler view for displaying gesture recognizer results

        Common.hideSystemBars(window)

        backgroundExecutor = Executors.newSingleThreadExecutor()

        //initialize the camera
        this.camera = Camera(
            this,
            viewBinding.pvCameraPreviewPractice
        )

        //initialize the gesture recognizer
        backgroundExecutor.execute {
            gestureRecognizerHelper = GestureRecognizerHelper(
                context = this,
                gestureRecognizerListener = this
            )

            //set the recognizer to the camera
            runOnUiThread {
                camera.setGestureRecognizer(gestureRecognizerHelper)

                // check/grant permissions for the camera
                if (camera.allPermissionsGranted()) {
                    camera.startCamera()
                } else {
                    camera.requestPermissions()
                }

                this.camera.setCaptureVideoButton(viewBinding.fabRecordPractice)
            }
        }

        //set listeners for buttons
        viewBinding.fabHomePractice.setOnClickListener {
            finish()
        }

        viewBinding.fabRecordPractice.setOnClickListener {
            camera.captureVideo()
        }

        viewBinding.fabSwitchCamPractice.setOnClickListener {
            camera.flipCamera()
        }

        viewBinding.btnSkipPractice.setOnClickListener {
            viewBinding.prompt.isVisible = false
            viewBinding.tvPracticeWord.text = "Fetching a new word..."
            lifecycleScope.launch {
                delay(2.seconds)
                viewBinding.prompt.isVisible = true
                changePracticeWord("New Word")
            }
        }

        viewBinding.tvCategory.text = this.intent.getStringExtra(CATEGORY_KEY)
        updateCheckWord()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        camera.configureRotation()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.closeCamera()
        backgroundExecutor.shutdown()
    }

    override fun onPause() {
        super.onPause()
        camera.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        if (camera.allPermissionsGranted()) {
            camera.startCamera()
        } else {
            camera.requestPermissions()
        }
    }

    override fun onError(error: String, errorCode: Int) {
        Log.i("Error", "Error: $error")
    }

    //Displays gesture recognizer results
    override fun onResults(resultBundle: GestureRecognizerHelper.ResultBundle) {
        runOnUiThread {

            if (viewBinding != null) {
                // Get the result of the recognized gesture
                val gestureCategories = resultBundle.results.first().gestures()
                if (gestureCategories.isNotEmpty()) {
                    sortResults(gestureCategories.first())

                    //logic for checking the letter
                    if (currLetter < practiceWord.length) {
                        val catString = this.nameCategories.firstOrNull()?.categoryName()
                        if (!catString.isNullOrEmpty()) {
                            val letter = catString[0]
                            if (letter == checkWord[currLetter]) {
                                currLetter++
                                updateStringSpan(currLetter)
                                //check if the next character is a space
                                if (currLetter < practiceWord.length) {
                                    while (currLetter < practiceWord.length &&
                                        !practiceWord[currLetter].isLetter() ) {
                                        currLetter++
                                    }
                                } else {
                                    /* Get the next word */
                                    viewBinding.prompt.isVisible = false
                                    viewBinding.tvPracticeWord.text = "Correct! Fetching the next word..."
                                    lifecycleScope.launch {
                                        delay(2.seconds)
                                        viewBinding.prompt.isVisible = true
                                        changePracticeWord("No Thanks")
                                    }
                                }
                            }
                        }
                    }
                } else {
                    sortResults(emptyList())
                }
            }
        }
    }

    private fun changePracticeWord(word: String) {
        this.practiceWord = word
        viewBinding.tvPracticeWord.text = practiceWord
        updateCheckWord()
        currLetter = 0
    }

    private fun updateStringSpan(currentLetterIndex: Int) {
        val spanString = SpannableString(practiceWord)
        val greenText = ForegroundColorSpan(Color.GREEN)
        spanString.setSpan(greenText, 0, currentLetterIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        viewBinding.tvPracticeWord.text = spanString
    }

    private fun updateCheckWord() {
        this.checkWord = this.practiceWord.uppercase()
    }

    private fun sortResults(categories: List<Category>?) {
        this.nameCategories = MutableList(1) { null }
        if (categories != null) {
            val sortedCategories = categories.sortedByDescending { it.score() }
            val min = min(sortedCategories.size, nameCategories.size )
            for (i in 0 until min) {
                nameCategories[i] = sortedCategories[i]
            }
            nameCategories.sortedBy { it?.index() }
        }
    }
}
