package com.itismob.group8.aslfingerspellingapp

import android.content.Intent
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
import com.itismob.group8.aslfingerspellingapp.dataclasses.NamesData
import com.itismob.group8.aslfingerspellingapp.retrofit.DatamuseRetrofitHelper
import com.itismob.group8.aslfingerspellingapp.retrofit.NameRetrofitHelper
import kotlinx.coroutines.Job
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
    private var isContinuingGame = false
    private var loadedGame: PreviousGame? = null
    private var gameStartTime: String = ""

    // Timer variables
    private var timeRemaining = 30 // 30 seconds per word
    private var timerJob: Job? = null
    private var isTimerRunning = false


    companion object {
        const val CATEGORY_KEY = "CATEGORY_KEY"
        const val CATEGORY_ENDPOINT = "CATEGORY_ENDPOINT"
        const val GAME_ID = "GAME_ID"
        const val TIMER_DURATION = 30 // 30 seconds per word
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityPlayCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Check if we're continuing a game or starting new
        val continueGameId = intent.getStringExtra(GAME_ID)
        if (continueGameId != null) {
            // Load existing game
            loadExistingGame(continueGameId)
        } else {
            // Start new game
            startNewGame()
        }

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

        // Set up UI listeners
        setupClickListeners()
    }

    private fun startNewGame() {
        // Get category data from intent
        val categoryName = this.intent.getStringExtra(CATEGORY_KEY) ?: "General"
        endpoint = this.intent.getStringExtra(CATEGORY_ENDPOINT) ?: "words?sp=?????"

        // Generate unique game ID
        gameId = "${categoryName}_${System.currentTimeMillis()}"
        isContinuingGame = false

        // Initialize fresh game state
        currentRound = 1
        score = 0
        totalRounds = 9
        gameStartTime = PreviousGame.getCurrentDateTime()
        timeRemaining = TIMER_DURATION // Reset timer for new game

        // Load words for the game
        if (endpoint == "na") {
            generateName()
        } else {
            getDatamuseWords()
        }

        viewBinding.tvCategoryPlay.text = categoryName
        updateGameDisplay()
    }

    private fun loadExistingGame(gameId: String) {
        this.gameId = gameId
        loadedGame = GameSaveManager.getGameById(this, gameId)

        if (loadedGame != null) {
            isContinuingGame = true

            // Check if game is completed by round count
            val isGameCompleted = loadedGame!!.currentRound >= loadedGame!!.totalRounds

            if (isGameCompleted) {
                finish()
                return
            } else {
                // Continue unfinished game from saved state
                currentRound = loadedGame!!.currentRound
                score = loadedGame!!.score
                totalRounds = loadedGame!!.totalRounds
                endpoint = loadedGame!!.endpoint
                gameStartTime = loadedGame!!.date
                viewBinding.tvCategoryPlay.text = loadedGame!!.category

                // Timer continues from where it was (not reset)
                timeRemaining = TIMER_DURATION

                if (endpoint == "na") {
                    generateName()
                } else {
                    getDatamuseWordsForContinue()
                }
            }
        } else {
            startNewGame()
        }

        updateGameDisplay()
    }

    private fun setupClickListeners() {
        viewBinding.btnSkipPlay.setOnClickListener {
            skipWord()
        }

        viewBinding.fabHomePlay.setOnClickListener {
            saveGameProgress()
            finish()
        }

        viewBinding.fabSwitchCamPlay.setOnClickListener {
            camera.flipCamera()
        }
    }

    private fun updateGameDisplay() {
        viewBinding.tvRound.text = "$currentRound/$totalRounds"
        viewBinding.tvScore.text = "$score"
        viewBinding.tvPlayWord.text = currentWord
        updateStringSpan(currLetter)
        updateTimerDisplay()
    }

    private fun updateTimerDisplay() {
        viewBinding.tvTimer.text = "$timeRemaining"

        // Change color based on time remaining
        when {
            timeRemaining <= 10 -> viewBinding.tvTimer.setTextColor(Color.RED)
            timeRemaining <= 20 -> viewBinding.tvTimer.setTextColor(Color.YELLOW)
            else -> viewBinding.tvTimer.setTextColor(Color.parseColor("#15C815")) //Green
        }
    }

    private fun startTimer() {
        stopTimer() // Stop any existing timer

        isTimerRunning = true
        timerJob = lifecycleScope.launch {
            while (timeRemaining > 0 && isTimerRunning) {
                delay(1000) // 1 second
                timeRemaining--
                runOnUiThread {
                    updateTimerDisplay()
                }
            }

            if (timeRemaining <= 0 && isTimerRunning) {
                // Timer ran out
                runOnUiThread {
                    onTimerExpired()
                }
            }
        }
    }

    private fun stopTimer() {
        isTimerRunning = false
        timerJob?.cancel()
        timerJob = null
    }

    private fun onTimerExpired() {
        stopTimer()

        // Auto-skip to next word when timer expires
        if (currentRound < totalRounds) {
            currentRound++
            loadNewWord()
        } else {
            // Timer expired on last word - end game
            endGame()
        }
    }

    private fun resetTimer() {
        timeRemaining = TIMER_DURATION
        updateTimerDisplay()
    }

    private fun skipWord() {
        stopTimer()
        if (currentRound < totalRounds) {
            currentRound++
            loadNewWord()
        } else {
            endGame()
        }
    }

    private fun loadNewWord() {
        stopTimer() // Stop current timer

        if (endpoint == "na") {
            generateName()
        } else if (wordsList.isNotEmpty()) {
            getRandomWordListIndex()
        } else {
            getDatamuseWords()
        }

        // Reset progress for new word
        currLetter = 0
        resetTimer()
        startTimer()
        updateGameDisplay()
        saveGameProgress()
    }

    private fun completeWord() {
        stopTimer()
        score += 10 // Points for completing a word

        if (currentRound < totalRounds) {
            currentRound++
            loadNewWord()
        } else {
            endGame()
        }
    }

    private fun endGame() {
        stopTimer()
        saveGameProgress()

        lifecycleScope.launch {
            viewBinding.tvPlayWord.text = "Game Complete! Score: $score"
            delay(2.seconds)
            finish()
        }
    }

    // Gesture Recognition Logic
    override fun onResults(resultBundle: GestureRecognizerHelper.ResultBundle) {
        runOnUiThread {
            if (viewBinding != null && isTimerRunning) {
                val gestureCategories = resultBundle.results.first().gestures()
                if (gestureCategories.isNotEmpty()) {
                    sortResults(gestureCategories.first())

                    if (currLetter < currentWord.length) {
                        val catString = this.nameCategories.firstOrNull()?.categoryName()
                        if (!catString.isNullOrEmpty()) {
                            val letter = catString[0]
                            if (letter == checkWord[currLetter]) {
                                currLetter++
                                updateStringSpan(currLetter)

                                if (currLetter < currentWord.length) {
                                    while (currLetter < currentWord.length &&
                                        !currentWord[currLetter].isLetter()) {
                                        currLetter++
                                    }
                                } else {
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

    // Word management methods
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
            override fun onResponse(call: Call<List<WordsData>?>, response: Response<List<WordsData>?>) {
                val responseData = response.body()
                if (!responseData.isNullOrEmpty()) {
                    wordsList = responseData
                    loadNewWord()
                }
            }

            override fun onFailure(call: Call<List<WordsData>?>, t: Throwable) {
                Log.e("PlayCamera", "Failed to fetch words: ${t.message}")
                wordsList = listOf(WordsData("hello", 100), WordsData("world", 100))
                loadNewWord()
            }
        })
    }

    private fun generateName() {
        NameRetrofitHelper.nameInterface.getName().enqueue(object : Callback<NamesData> {
            override fun onResponse(call: Call<NamesData?>, response: Response<NamesData?>) {
                val responseData = response.body()
                if (responseData != null) {
                    changePracticeWord(responseData.name)
                } else {
                    changePracticeWord("Alex")
                }
            }

            override fun onFailure(call: Call<NamesData?>, t: Throwable) {
                Log.e("PlayCamera", "Failed to fetch name: ${t.message}")
                val fallbackNames = listOf("Alex", "Taylor", "Jordan", "Casey", "Riley")
                changePracticeWord(fallbackNames.random())
            }
        })
    }

    private fun getDatamuseWordsForContinue() {
        getDatamuseWords()
    }

    // Game Saving Logic
    private fun saveGameProgress() {
        val categoryName = viewBinding.tvCategoryPlay.text.toString()
        val gameRecord = PreviousGame(
            imageResId = R.drawable.ic_hand_a,
            category = categoryName,
            currentRound = currentRound,
            totalRounds = totalRounds,
            date = if (isContinuingGame && loadedGame != null) loadedGame!!.date else gameStartTime,
            score = score,
            gameId = gameId,
            endpoint = endpoint
        )

        GameSaveManager.saveGame(this, gameRecord)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        camera.closeCamera()
        backgroundExecutor.shutdown()
    }

    override fun onPause() {
        super.onPause()
        stopTimer()
        camera.stopCamera()
        saveGameProgress()
    }

    override fun onResume() {
        super.onResume()
        if (camera.allPermissionsGranted()) {
            camera.startCamera()
        } else {
            camera.requestPermissions()
        }
        // Timer will be started when word loads
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        camera.configureRotation()
    }
}