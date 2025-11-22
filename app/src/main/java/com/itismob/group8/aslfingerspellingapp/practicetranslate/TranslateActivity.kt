package com.itismob.group8.aslfingerspellingapp.practicetranslate

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.tasks.components.containers.Category
import com.itismob.group8.aslfingerspellingapp.libraries.Camera
import com.itismob.group8.aslfingerspellingapp.common.Common
import com.itismob.group8.aslfingerspellingapp.libraries.GestureRecognizerHelper
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityTranslateBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TranslateActivity : AppCompatActivity(), GestureRecognizerHelper.GestureRecognizerListener {
    private lateinit var viewBinding: ActivityTranslateBinding
    private lateinit var camera: Camera
    private lateinit var backgroundExecutor: ExecutorService
    private lateinit var gestureRecognizerHelper: GestureRecognizerHelper
    //private lateinit var recyclerView: RecyclerView
    //private val defaultNumResults = 1
    private var currentWord = ""
    /*private val gestureRecognizerResultAdapter: TranslateGestureRecognizerResultsAdapter by lazy {
        TranslateGestureRecognizerResultsAdapter().apply {
            updateAdapterSize(defaultNumResults)
        }
    } */
    private var currLetter: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewBinding = ActivityTranslateBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.tvTranslationOutput.text = currentWord

        //recycler view for displaying gesture recognizer results
        /*
        this.recyclerView = viewBinding.rvTranslateResult
        this.recyclerView.adapter = gestureRecognizerResultAdapter
        this.recyclerView.layoutManager = LinearLayoutManager(this) */

        Common.Companion.hideSystemBars(window)

        backgroundExecutor = Executors.newSingleThreadExecutor()

        //initialize the camera
        this.camera = Camera(
            this,
            viewBinding.pvCameraPreviewTranslate
        )

        //initialize gesture recognizer
        backgroundExecutor.execute {
            gestureRecognizerHelper = GestureRecognizerHelper(
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

        viewBinding.fabBackspaceTranslate.setOnClickListener {
            if (currentWord.isNotEmpty()) {
                currentWord = currentWord.dropLast(1)
                viewBinding.tvTranslationOutput.text = currentWord
            }
        }

        viewBinding.fabAddLetter.setOnClickListener {
            //get the current letter
            val letter = currLetter
            if (!letter.isNullOrBlank()) {
                currentWord += letter
                viewBinding.tvTranslationOutput.text = currentWord
            }
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
        Log.i("ERROR_GESTURE_RECOGNIZER", "Error: " + error)
    }

    //Displays results of Gesture Recognizer
    override fun onResults(resultBundle: GestureRecognizerHelper.ResultBundle) {
        runOnUiThread {
            if (viewBinding != null) {
                // Show result of recognized gesture
                val gestureCategories = resultBundle.results.first().gestures()
                if (gestureCategories.isNotEmpty()) {
                    updateResults(
                        gestureCategories.first()
                    )
                } else {
                    updateResults(emptyList())
                }
            }
        }
    }

    fun updateResults(categories: List<Category>?) {
        if (categories != null && categories.isNotEmpty()) {
            viewBinding.tvGROutput.text = categories[0].categoryName()
            currLetter = categories[0].categoryName()
        } else {
            currLetter = ""
            viewBinding.tvGROutput.text = ""
        }
    }
}