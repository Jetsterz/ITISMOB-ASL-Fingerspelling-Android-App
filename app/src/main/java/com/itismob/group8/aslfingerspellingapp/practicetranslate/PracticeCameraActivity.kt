package com.itismob.group8.aslfingerspellingapp.practicetranslate

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
import com.itismob.group8.aslfingerspellingapp.libraries.Camera
import com.itismob.group8.aslfingerspellingapp.common.Common
import com.itismob.group8.aslfingerspellingapp.libraries.GestureRecognizerHelper
import com.itismob.group8.aslfingerspellingapp.dataclasses.NamesData
import com.itismob.group8.aslfingerspellingapp.dataclasses.WordsData
import com.itismob.group8.aslfingerspellingapp.retrofit.DatamuseRetrofitHelper
import com.itismob.group8.aslfingerspellingapp.retrofit.NameRetrofitHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class PracticeCameraActivity : AppCompatActivity(), GestureRecognizerHelper.GestureRecognizerListener {
    private lateinit var viewBinding: ActivityPracticeCameraBinding
    private lateinit var camera: Camera
    private lateinit var backgroundExecutor: ExecutorService
    private lateinit var gestureRecognizerHelper: GestureRecognizerHelper
    private var practiceWord = "Fetching words..."
    private var currLetter = 0
    private var checkWord = "JJ"
    private var nameCategories: MutableList<Category?> = mutableListOf()
    private var wordsList: List<WordsData> = emptyList()
    private lateinit var endpoint: String
    private var api: Int = 0

    companion object {
        const val CATEGORY_KEY = "CATEGORY_KEY"
        const val CATEGORY_ENDPOINT = "CATEGORY_ENDPOINT"
        const val CATEGORY_API = "CATEGORY_API" //specify what api to use
        const val DATAMUSE_API = 1
        const val LIST_OF_NAMES_API = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewBinding = ActivityPracticeCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        endpoint = this.intent.getStringExtra(CATEGORY_ENDPOINT)!!
        api = this.intent.getIntExtra(CATEGORY_API,
            DATAMUSE_API)

        Common.Companion.hideSystemBars(window)

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

        //setting the first practice word
        when (api) {
            //fetch the list of words
            DATAMUSE_API -> {
                getDatamuseWords()
            }

            LIST_OF_NAMES_API -> {
                //do {
                    generateName()
               // } while (this.checkWord.contains('J') ||
                  //  this.checkWord.contains('Z'))
            }
        }

        viewBinding.prompt.text = "Spell the word below in sign language!"

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
                setRandomPracticeWord()
                viewBinding.prompt.isVisible = true
            }
        }

        viewBinding.tvCategory.text = this.intent.getStringExtra(CATEGORY_KEY)
    }

    private fun setRandomPracticeWord() {
        when (api) {
            DATAMUSE_API -> {
                getRandomWordListIndex()
            }
            LIST_OF_NAMES_API -> {
                //do {
                generateName()
               // } while (this.checkWord.contains('J') ||
                  //  this.checkWord.contains('Z'))
            }
            else -> changePracticeWord("Hello")
        }
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
                                        setRandomPracticeWord()
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

    private fun getRandomWordListIndex() {
        var index = 0
        do {
            index = Random.nextInt(0, wordsList.size)
        } while (wordsList[index].word.uppercase().contains('J') ||
            wordsList[index].word.uppercase().contains('Z'))
        changePracticeWord(wordsList[index].word)
    }
    private fun getDatamuseWords() {
        DatamuseRetrofitHelper.datamuseInterface.getWords(endpoint).enqueue(object : Callback<List<WordsData>> {
            override fun onResponse(
                call: Call<List<WordsData>?>,
                response: Response<List<WordsData>?>
            ) {
                val responseData = response.body()
                if (!responseData.isNullOrEmpty()) {
                    wordsList = responseData
                    changePracticeWord(wordsList[0].word)
                }
            }

            override fun onFailure(
                call: Call<List<WordsData>?>,
                t: Throwable
            ) {

            }
        })
    }
    private fun generateName() {
        NameRetrofitHelper.nameInterface.getName().enqueue(object : Callback<NamesData> {
            override fun onResponse(
                call: Call<NamesData?>,
                response: Response<NamesData?>
            ) {
                val responseData = response.body()
                if (responseData != null) {
                    changePracticeWord(responseData.name)
                }
            }

            override fun onFailure(
                call: Call<NamesData?>,
                t: Throwable
            ) {
            }
        })
    }

}
