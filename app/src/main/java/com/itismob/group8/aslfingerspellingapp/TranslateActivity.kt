package com.itismob.group8.aslfingerspellingapp

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityTranslateBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TranslateActivity : AppCompatActivity(), GestureRecognizerHelper.GestureRecognizerListener {
    private lateinit var viewBinding: ActivityTranslateBinding
    private lateinit var camera: Camera
    private lateinit var backgroundExecutor: ExecutorService
    private lateinit var gestureRecognizerHelper: GestureRecognizerHelper
    private lateinit var recyclerView: RecyclerView
    private val defaultNumResults = 1
    private val gestureRecognizerResultAdapter: TranslateGestureRecognizerResultsAdapter by lazy {
        TranslateGestureRecognizerResultsAdapter().apply {
            updateAdapterSize(defaultNumResults)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityTranslateBinding.inflate(layoutInflater)

        //recycler view for displaying gesture recognizer results
        this.recyclerView = viewBinding.rvTranslateResult
        this.recyclerView.adapter = gestureRecognizerResultAdapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)

        backgroundExecutor = Executors.newSingleThreadExecutor()

        //initialize the camera
        this.camera = Camera(
            this,
            viewBinding.pvCameraPreviewTranslate
        )

        //initialize gesture recognizer
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

            //set the gesture recognizer to the camera
            runOnUiThread {
                camera.setGestureRecognizer(gestureRecognizerHelper)
            }
        }

        this.camera.setCaptureVideoButton(viewBinding.fabRecordTranslate)

        //get camera permissions
        if (camera.allPermissionsGranted()) {
            camera.startCamera()
        } else {
            camera.requestPermissions()
        }

        viewBinding.fabHomeTranslate.setOnClickListener {
            finish()
        }

        viewBinding.fabRecordTranslate.setOnClickListener {
            camera.captureVideo()
        }

        viewBinding.fabSwitchCamTranslate.setOnClickListener {
            camera.flipCamera()
        }

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
        Log.i("ERROR_GESTURE_RECOGNIZER", "Error: " + error)
    }

    //Displays results of Gesture Recognizer
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

                //displays the mapping of the hand; TODO:remove before submission
                viewBinding.translateOverlay.setResults(
                    resultBundle.results.first(),
                    resultBundle.inputImageHeight,
                    resultBundle.inputImageWidth,
                    RunningMode.LIVE_STREAM
                )
                viewBinding.translateOverlay.invalidate()
            }
        }
    }
}