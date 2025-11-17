package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SaveActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        val playButton: Button = findViewById(R.id.btnPlayMain)
        playButton.setOnClickListener {
            Toast.makeText(this, "Main game play clicked!", Toast.LENGTH_SHORT).show()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerPreviousGames)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load actual saved games
        val savedGames = GameSaveManager.loadAllGames(this)

        // If no saved games, show sample data
        val gameList = if (savedGames.isNotEmpty()) {
            savedGames
        } else {
            listOf(
                PreviousGame(R.drawable.ic_hand_a, "Animals", 5, "Oct 20, 2025", 45, true),
                PreviousGame(R.drawable.ic_hand_a, "Fruits", 7, "Oct 21, 2025", 68, true),
                PreviousGame(R.drawable.ic_hand_a, "Colors", 4, "Oct 22, 2025", 32, false)
            )
        }

        recyclerView.adapter = PreviousGameAdapter(this, gameList)
    }
}