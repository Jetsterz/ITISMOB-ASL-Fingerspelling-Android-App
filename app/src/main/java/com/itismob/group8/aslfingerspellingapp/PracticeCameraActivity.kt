package com.itismob.group8.aslfingerspellingapp

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityPracticeCameraBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class PracticeCameraActivity : AppCompatActivity(), GestureRecognizerHelper.GestureRecognizerListener {
    private lateinit var viewBinding: ActivityPracticeCameraBinding
    private lateinit var camera: Camera
    private lateinit var backgroundExecutor: ExecutorService
    private lateinit var gestureRecognizerHelper: GestureRecognizerHelper

    //for displaying gesture recognizer results
    private lateinit var recyclerView: RecyclerView
    private val defaultNumResults = 1
    private var practiceWord = "Hello World" //

    //for displaying gesture recognizer results
    private val gestureRecognizerResultAdapter: PracticeGestureRecognizerResultsAdapter by lazy {
        PracticeGestureRecognizerResultsAdapter().apply {
            updateAdapterSize(defaultNumResults)
        }
    }

    companion object {
        const val CATEGORY_KEY = "CATEGORY_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewBinding = ActivityPracticeCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //recycler view for displaying gesture recognizer results
        this.recyclerView = viewBinding.rvPracticeResults
        this.recyclerView.adapter = gestureRecognizerResultAdapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)

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

        viewBinding.tvCategory.text = this.intent.getStringExtra(CATEGORY_KEY)

        //span text for colored letters
        val spanString = SpannableString(practiceWord)

        val greenText = ForegroundColorSpan(Color.GREEN)

        spanString.setSpan(greenText, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        viewBinding.tvPracticeWord.text = spanString
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
        Log.i("Error", "Error: " + error)
    }

    //Displays gesture recognizer results
    override fun onResults(resultBundle: GestureRecognizerHelper.ResultBundle) {
        runOnUiThread {
            if (viewBinding != null) {
                // Show result of recognized gesture
                val gestureCategories = resultBundle.results.first().gestures()
                if (gestureCategories.isNotEmpty()) {
                    gestureRecognizerResultAdapter.updateResults(
                        gestureCategories.first()
                    )
                } else {
                    gestureRecognizerResultAdapter.updateResults(emptyList())
                }
            }
        }
    }
}