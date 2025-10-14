package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityTranslateBinding

class TranslateActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityTranslateBinding
    private lateinit var camera: Camera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityTranslateBinding.inflate(layoutInflater)

        this.camera = Camera(this, viewBinding.pvCameraPreviewTranslate)
        this.camera.setCaptureVideoButton(viewBinding.fabRecordTranslate)

        if (camera.allPermissionsGranted()) {
            camera.startCamera()
        } else {
            camera.requestPermissions()
        }

        viewBinding.fabHomeTranslate.setOnClickListener {
            finish()
            camera.closeCamera()
        }

        viewBinding.fabRecordTranslate.setOnClickListener {
            camera.captureVideo()
        }

        viewBinding.fabSwitchCamTranslate.setOnClickListener {
            camera.flipCamera()
        }

        setContentView(viewBinding.root)
    }
}