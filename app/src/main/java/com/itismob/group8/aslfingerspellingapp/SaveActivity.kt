package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SaveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        val playButton: Button = findViewById(R.id.btnPlayMain)

        playButton.setOnClickListener {
            Toast.makeText(this, "Play button clicked!", Toast.LENGTH_SHORT).show()
        }
    }
}
