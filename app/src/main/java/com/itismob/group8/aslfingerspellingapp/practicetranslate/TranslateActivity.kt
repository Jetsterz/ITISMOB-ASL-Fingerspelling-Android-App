package com.itismob.group8.aslfingerspellingapp.practicetranslate

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.tasks.components.containers.Category
import com.itismob.group8.aslfingerspellingapp.R
import com.itismob.group8.aslfingerspellingapp.libraries.Camera
import com.itismob.group8.aslfingerspellingapp.common.Common
import com.itismob.group8.aslfingerspellingapp.libraries.GestureRecognizerHelper
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityTranslateBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.text.uppercaseChar

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

        viewBinding.ibSignLanguageAlphabet.setOnClickListener {
            showDialogwithIcon(this)
        }
    }

    @SuppressLint("MissingInflatedId")
    // Open the Dialog With an Alert Icon
    fun showDialogwithIcon(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.AlertDialogTheme)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_layout_translate, null)

        // Find the ImageView and set the icon
        val iconImage: ImageView = dialogView.findViewById(R.id.dialog_icon)
        iconImage.setImageResource(R.drawable.asl_alphabet)

        // Set the custom layout to the dialog
        builder.setView(dialogView)
            .setTitle("Alphabet in Sign Language")

        // Add a negative button to cancel the dialog
        builder.setNegativeButton("CLOSE") { dialog, _ ->
            dialog.dismiss()
        }

        // Show the dialog
        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.color.alertdialog_background)
        dialog.show()

        val negativeButton: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setTextColor(resources.getColor(R.color.alertdialog_buttoncolor)) // Set negative button text to red
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