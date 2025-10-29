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
        viewBinding.btnSkipPlay.setOnClickListener {
            // TODO: Implement skip logic (e.g., move to next word or round)
        }

        viewBinding.tvRound.text = "Round: 1/9"
        viewBinding.tvScore.text = "Score: 0"
        viewBinding.tvPlayWord.text = "No Thanks"
        viewBinding.fabHomePlay.setOnClickListener {
            finish()
            camera.closeCamera()
        }

        this.camera = Camera(this, viewBinding.pvCameraPreviewPlay)

        if (camera.allPermissionsGranted()) {
            camera.startCamera()
        } else {
            camera.requestPermissions()
        }


        viewBinding.fabSwitchCamPlay.setOnClickListener {
            camera.flipCamera()
        }

        viewBinding.tvCategoryPlay.text = this.intent.getStringExtra(CATEGORY_KEY)
        setContentView(viewBinding.root)
    }


}