package com.itismob.group8.aslfingerspellingapp

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.mediapipe.tasks.components.containers.Category
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityPlayCameraBinding
import com.itismob.group8.aslfingerspellingapp.libraries.Camera
import com.itismob.group8.aslfingerspellingapp.libraries.GestureRecognizerHelper
import com.itismob.group8.aslfingerspellingapp.dataclasses.WordsData
import com.itismob.group8.aslfingerspellingapp.retrofit.DatamuseRetrofitHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PlayCameraActivity : AppCompatActivity(), GestureRecognizerHelper.GestureRecognizerListener {
    private lateinit var viewBinding: ActivityPlayCameraBinding
    private lateinit var camera: Camera
    private lateinit var backgroundExecutor: ExecutorService
    private lateinit var gestureRecognizerHelper: GestureRecognizerHelper

    // Game state variables
    private var currentWord = "Fetching words..."
    private var currLetter = 0
    private var checkWord = "JJ"
    private var nameCategories: MutableList<Category?> = mutableListOf()
    private var wordsList: List<WordsData> = emptyList()
    private lateinit var endpoint: String

    // Game progress tracking
    private var currentRound = 1
    private var totalRounds = 9
    private var score = 0
    private var gameId: String = ""

    companion object {
        const val CATEGORY_KEY = "CATEGORY_KEY"
        const val CATEGORY_ENDPOINT = "CATEGORY_ENDPOINT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityPlayCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Get category data from intent
        val categoryName = this.intent.getStringExtra(CATEGORY_KEY) ?: "General"
        endpoint = this.intent.getStringExtra(CATEGORY_ENDPOINT) ?: "words?sp=?????"

        // Generate unique game ID
        gameId = "${categoryName}_${System.currentTimeMillis()}"

        // Initialize game state
        updateGameDisplay()

        backgroundExecutor = Executors.newSingleThreadExecutor()

        // Initialize camera
        this.camera = Camera(this, viewBinding.pvCameraPreviewPlay)

        // Initialize gesture recognizer
        backgroundExecutor.execute {
            gestureRecognizerHelper = GestureRecognizerHelper(
                context = this,
                gestureRecognizerListener = this
            )

            runOnUiThread {
                camera.setGestureRecognizer(gestureRecognizerHelper)

                if (camera.allPermissionsGranted()) {
                    camera.startCamera()
                } else {
                    camera.requestPermissions()
                }
            }
        }

        // Load words for the game
        getDatamuseWords()

        // Set up UI listeners
        setupClickListeners(categoryName)
    }

    private fun setupClickListeners(categoryName: String) {
        viewBinding.btnSkipPlay.setOnClickListener {
            skipWord()
        }

        viewBinding.fabHomePlay.setOnClickListener {
            saveGameProgress(categoryName)
            finish()
        }

        viewBinding.fabSwitchCamPlay.setOnClickListener {
            camera.flipCamera()
        }

        viewBinding.tvCategoryPlay.text = categoryName
    }

    private fun updateGameDisplay() {
        viewBinding.tvRound.text = "$currentRound/$totalRounds"
        viewBinding.tvScore.text = "$score"
        viewBinding.tvPlayWord.text = currentWord
        updateStringSpan(currLetter)
    }

    private fun skipWord() {
        // Penalty for skipping? Maybe reduce score?
        if (currentRound < totalRounds) {
            currentRound++
            loadNewWord()
        } else {
            endGame()
        }
    }

    private fun loadNewWord() {
        if (wordsList.isNotEmpty()) {
            getRandomWordListIndex()
            updateGameDisplay()
        }
    }

    private fun completeWord() {
        score += 10 // Points for completing a word
        if (currentRound < totalRounds) {
            currentRound++
            loadNewWord()
        } else {
            endGame()
        }
    }

    private fun endGame() {
        val categoryName = viewBinding.tvCategoryPlay.text.toString()
        saveGameProgress(categoryName, true)

        // Show completion message
        lifecycleScope.launch {
            viewBinding.tvPlayWord.text = "Game Complete! Score: $score"
            delay(3.seconds)
            finish()
        }
    }

    // Gesture Recognition Logic (from PracticeCameraActivity)
    override fun onResults(resultBundle: GestureRecognizerHelper.ResultBundle) {
        runOnUiThread {
            if (viewBinding != null) {
                val gestureCategories = resultBundle.results.first().gestures()
                if (gestureCategories.isNotEmpty()) {
                    sortResults(gestureCategories.first())

                    // Check letter recognition
                    if (currLetter < currentWord.length) {
                        val catString = this.nameCategories.firstOrNull()?.categoryName()
                        if (!catString.isNullOrEmpty()) {
                            val letter = catString[0]
                            if (letter == checkWord[currLetter]) {
                                currLetter++
                                updateStringSpan(currLetter)

                                // Skip non-letter characters
                                if (currLetter < currentWord.length) {
                                    while (currLetter < currentWord.length &&
                                        !currentWord[currLetter].isLetter()) {
                                        currLetter++
                                    }
                                } else {
                                    // Word completed
                                    completeWord()
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

    override fun onError(error: String, errorCode: Int) {
        Log.i("PlayCameraError", "Error: $error")
    }

    // Word management methods (from PracticeCameraActivity)
    private fun changePracticeWord(word: String) {
        this.currentWord = word
        this.checkWord = word.uppercase()
        this.currLetter = 0
        updateGameDisplay()
    }

    private fun updateStringSpan(currentLetterIndex: Int) {
        val spanString = SpannableString(currentWord)
        val greenText = ForegroundColorSpan(Color.GREEN)
        spanString.setSpan(greenText, 0, currentLetterIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        viewBinding.tvPlayWord.text = spanString
    }

    private fun sortResults(categories: List<Category>?) {
        this.nameCategories = MutableList(1) { null }
        if (categories != null) {
            val sortedCategories = categories.sortedByDescending { it.score() }
            val min = min(sortedCategories.size, nameCategories.size)
            for (i in 0 until min) {
                nameCategories[i] = sortedCategories[i]
            }
            nameCategories.sortedBy { it?.index() }
        }
    }

    private fun getRandomWordListIndex() {
        if (wordsList.isEmpty()) return

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
                    loadNewWord()
                }
            }

            override fun onFailure(call: Call<List<WordsData>?>, t: Throwable) {
                Log.e("PlayCamera", "Failed to fetch words: ${t.message}")
                // Fallback words
                wordsList = listOf(WordsData("hello", 100), WordsData("world", 100))
                loadNewWord()
            }
        })
    }

    // Game Saving Logic
    private fun saveGameProgress(categoryName: String, isCompleted: Boolean = false) {
        val gameRecord = PreviousGame(
            imageResId = R.drawable.ic_hand_a, // Default image
            category = categoryName,
            rounds = if (isCompleted) totalRounds else currentRound - 1,
            date = Date().toString(),
            score = score,
            isCompleted = isCompleted,
            gameId = gameId
        )

        GameSaveManager.saveGame(this, gameRecord)
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.closeCamera()
        backgroundExecutor.shutdown()
    }

    override fun onPause() {
        super.onPause()
        camera.stopCamera()
        // Auto-save on pause
        val categoryName = viewBinding.tvCategoryPlay.text.toString()
        saveGameProgress(categoryName)
    }

    override fun onResume() {
        super.onResume()
        if (camera.allPermissionsGranted()) {
            camera.startCamera()
        } else {
            camera.requestPermissions()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        camera.configureRotation()
    }
}