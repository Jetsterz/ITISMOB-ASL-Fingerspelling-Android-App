package com.itismob.group8.aslfingerspellingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SaveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        val playButton: Button = findViewById(R.id.btnPlayMain)
        playButton.setOnClickListener {
            // Redirect to category selection for new game
            val intent = Intent(this, PlayCategoryActivity::class.java)
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerPreviousGames)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load actual saved games
        val savedGames = GameSaveManager.loadAllGames(this)

        // Sort by date (newest first)
        val sortedGames = savedGames.sortedByDescending { it.date }

        recyclerView.adapter = PreviousGameAdapter(this, sortedGames)
    }
}