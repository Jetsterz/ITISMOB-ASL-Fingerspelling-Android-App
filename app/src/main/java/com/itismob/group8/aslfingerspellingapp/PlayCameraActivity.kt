package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityPlayCameraBinding

class PlayCameraActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityPlayCameraBinding
    private lateinit var camera: Camera

    companion object {
        const val CATEGORY_KEY = "CATEGORY_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        viewBinding = ActivityPlayCameraBinding.inflate(layoutInflater)
        viewBinding.fabHomePlay.setOnClickListener {
            finish()
            camera.closeCamera()
        }

        this.camera = Camera(this, viewBinding.pvCameraPreviewPlay)
        this.camera.setCaptureVideoButton(viewBinding.fabRecordPlay)

        if (camera.allPermissionsGranted()) {
            camera.startCamera()
        } else {
            camera.requestPermissions()
        }

        viewBinding.fabRecordPlay.setOnClickListener {
            camera.captureVideo()
        }

        viewBinding.fabSwitchCamPlay.setOnClickListener {
            camera.flipCamera()
        }

        viewBinding.tvCategoryPlay.text = this.intent.getStringExtra(CATEGORY_KEY)
        setContentView(viewBinding.root)
    }


}