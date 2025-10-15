package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityPracticeCameraBinding

class PracticeCameraActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityPracticeCameraBinding
    private lateinit var camera: Camera

    companion object {
        const val CATEGORY_KEY = "CATEGORY_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityPracticeCameraBinding.inflate(layoutInflater)
        viewBinding.fabHomePractice.setOnClickListener {
            finish()
            camera.closeCamera()
        }

        this.camera = Camera(this, viewBinding.pvCameraPreviewPractice)
        this.camera.setCaptureVideoButton(viewBinding.fabRecordPractice)

        if (camera.allPermissionsGranted()) {
            camera.startCamera()
        } else {
            camera.requestPermissions()
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


}