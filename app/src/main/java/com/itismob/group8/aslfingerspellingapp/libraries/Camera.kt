package com.itismob.group8.aslfingerspellingapp.libraries

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import android.view.Surface
import androidx.camera.core.AspectRatio
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.content.PermissionChecker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.itismob.group8.aslfingerspellingapp.R
import java.text.SimpleDateFormat
import java.util.Locale

/* Code based from https://developer.android.com/codelabs/camerax-getting-started#0
* https://ngengesenior.medium.com/seamlessly-switching-camera-lenses-during-video-recording-with-camerax-on-android-fcb597ed8236
* https://github.com/google-ai-edge/mediapipe-samples/tree/main/examples/gesture_recognizer/android
* */
class Camera(private val activity: AppCompatActivity, private val cameraPreview: PreviewView) {
    private var videoCapture: VideoCapture<Recorder>? = null
    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
    private var recording: Recording? = null
    private var cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var imageAnalyzer: ImageAnalysis
    private lateinit var preview: Preview
    private lateinit var recorder: Recorder
    private lateinit var captureVideoButton: FloatingActionButton
    private var gestureRecognizerHelper: GestureRecognizerHelper ? = null

    companion object {
        const val TAG = "Sign-ify"
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    fun setGestureRecognizer(helper: GestureRecognizerHelper) {
        this.gestureRecognizerHelper = helper
    }
    fun configureRotation() {
        imageAnalyzer.targetRotation = cameraPreview.display.rotation
    }
    fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

     fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            activity.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            val rotation = cameraPreview.display?.rotation ?: Surface.ROTATION_0

            // Preview
            preview = Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(rotation)
                .build()
                .also {
                    it.surfaceProvider = cameraPreview.surfaceProvider
                }

            recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            // Select back camera as a default
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding

                cameraProvider.unbindAll()

                //imageAnalyzer to be bound to camera for recognizing gestures
                imageAnalyzer =
                    ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3) //AspectRatio.RATIO_4_3
                        .setTargetRotation(rotation)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                        .build()
                        // The analyzer can then be assigned to the instance
                        .also {
                            it.setAnalyzer(cameraExecutor) { image ->
                                recognizeHand(image)
                            }
                        }

                // Bind use cases to camera
                cameraProvider
                    .bindToLifecycle(activity, cameraSelector, preview, videoCapture, imageAnalyzer)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(activity))
    }

    private fun recognizeHand(imageProxy: ImageProxy) {
        val helper = gestureRecognizerHelper
        if (helper != null) {
            gestureRecognizerHelper?.recognizeLiveStream(
                imageProxy,
                cameraSelector)
        } else {
            imageProxy.close()
        }
    }

    //to be called when flipping cameras
    private fun resetImageAnalyzer() {
        imageAnalyzer =
            ImageAnalysis.Builder().setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .setTargetRotation(cameraPreview.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(cameraExecutor) { image ->
                        recognizeHand(image)
                    }
                }
    }

    private val activityResultLauncher =
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && !it.value)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(activity.baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }

    fun setCaptureVideoButton(button: FloatingActionButton) {
        this.captureVideoButton = button
    }

    fun captureVideo() {
        val videoCapture = this.videoCapture ?: return

        captureVideoButton.isEnabled = false

        val curRecording = recording
        if (curRecording != null) {
            // Stop the current recording session.
            curRecording.stop()
            recording = null
            return
        }

        // create and start a new recording session
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(activity.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()
        recording = videoCapture.output
            .prepareRecording(activity, mediaStoreOutputOptions)
            .asPersistentRecording()
            .apply {
                if (PermissionChecker.checkSelfPermission(activity,
                        Manifest.permission.RECORD_AUDIO) ==
                    PermissionChecker.PERMISSION_GRANTED)
                {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(activity)) { recordEvent ->
                when(recordEvent) {
                    is VideoRecordEvent.Start -> {
                        captureVideoButton.apply {
                            setImageResource(R.drawable.stop_recording)
                            isEnabled = true
                        }
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val msg = "Video capture succeeded: " +
                                    "${recordEvent.outputResults.outputUri}"
                            Toast.makeText(activity.baseContext, msg, Toast.LENGTH_SHORT)
                                .show()
                            Log.d(TAG, msg)
                        } else {
                            recording?.close()
                            recording = null
                            Log.e(
                                TAG, "Video capture ends with error: " +
                                    "${recordEvent.error}")
                        }
                       captureVideoButton.apply {
                            setImageResource(R.drawable.record_button)
                            isEnabled = true
                        }
                    }
                }
            }
    }

    fun flipCamera() {
        // change the Camera lens
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        val hasRecording = recording != null

        if (hasRecording) recording?.pause()

        //rebind all use cases
        if (::cameraProvider.isInitialized) {
            cameraProvider.unbindAll()
            resetImageAnalyzer() //reset based on the new selected camera
            cameraProvider.bindToLifecycle(activity, cameraSelector, preview, videoCapture,imageAnalyzer)
        }

        if (hasRecording) recording?.resume()
    }

    fun stopCamera() {
        try {
            if (::cameraProvider.isInitialized) {
                cameraProvider.unbindAll()
            }
        } catch (e: Exception) {
            Log.w(TAG, "stopCamera: unbindAll failed: ${e.message}")
        }
    }

    fun closeCamera() {
        this.gestureRecognizerHelper?.let { helper ->
            cameraExecutor.execute {
                helper.clearGestureRecognizer()
            }
        }
        this.cameraExecutor.shutdown()
    }
}