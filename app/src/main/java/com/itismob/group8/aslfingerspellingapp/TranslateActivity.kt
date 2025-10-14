package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityTranslateBinding

class TranslateActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityTranslateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityTranslateBinding.inflate(layoutInflater)

        viewBinding.fabHomeTranslate.setOnClickListener {
            finish()
        }

        setContentView(viewBinding.root)

    }
}