package com.itismob.group8.aslfingerspellingapp

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
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

        viewBinding = ActivityPracticeCameraBinding.inflate(layoutInflater)

        //recycler view for displaying gesture recognizer results
        this.recyclerView = viewBinding.rvPracticeResults
        this.recyclerView.adapter = gestureRecognizerResultAdapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)

        backgroundExecutor = Executors.newSingleThreadExecutor()

        //initialize the camera
        this.camera = Camera(
            this,
            viewBinding.pvCameraPreviewPractice
        )

        //initialize the gesture recognizer
        backgroundExecutor.execute {
            gestureRecognizerHelper = GestureRecognizerHelper(
                minHandDetectionConfidence = GestureRecognizerHelper.DEFAULT_HAND_DETECTION_CONFIDENCE,
                minHandTrackingConfidence = GestureRecognizerHelper.DEFAULT_HAND_TRACKING_CONFIDENCE,
                minHandPresenceConfidence = GestureRecognizerHelper.DEFAULT_HAND_PRESENCE_CONFIDENCE,
                currentDelegate = GestureRecognizerHelper.DELEGATE_CPU,
                runningMode = RunningMode.LIVE_STREAM,
                context = this,
                gestureRecognizerListener = this
            )

            //set the recognizer to the camera
            runOnUiThread {
                camera.setGestureRecognizer(gestureRecognizerHelper)
            }
        }

        // check/grant permissions for the camera
        if (camera.allPermissionsGranted()) {
            camera.startCamera()
        } else {
            camera.requestPermissions()
        }

        this.camera.setCaptureVideoButton(viewBinding.fabRecordPractice)

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
        setContentView(viewBinding.root)
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