package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityPracticeCameraBinding

class PracticeCameraActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityPracticeCameraBinding

    companion object {
        const val CATEGORY_KEY = "CATEGORY_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityPracticeCameraBinding.inflate(layoutInflater)
        viewBinding.fabHomePractice.setOnClickListener {
            finish()
        }
        viewBinding.tvCategory.text = this.intent.getStringExtra(CATEGORY_KEY)

        setContentView(viewBinding.root)
    }
}