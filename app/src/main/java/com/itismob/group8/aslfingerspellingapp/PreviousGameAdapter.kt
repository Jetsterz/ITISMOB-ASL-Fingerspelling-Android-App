package com.itismob.group8.aslfingerspellingapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PreviousGameAdapter(
    private val context: Context,
    private val gameList: List<PreviousGame>
) : RecyclerView.Adapter<PreviousGameAdapter.GameViewHolder>() {

    inner class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgGame: ImageView = view.findViewById(R.id.imgGame)
        val txtCategory: TextView = view.findViewById(R.id.txtCategory)
        val txtRounds: TextView = view.findViewById(R.id.txtRounds)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtScore: TextView = view.findViewById(R.id.txtScore)
        val btnPlay: Button = view.findViewById(R.id.btnPlay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_previous_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = gameList[position]
        holder.imgGame.setImageResource(game.imageResId)
        holder.txtCategory.text = "Category: ${game.category}"
        holder.txtRounds.text = "Round: ${game.currentRound}/${game.totalRounds}"
        holder.txtDate.text = "Date: ${game.date}"
        holder.txtScore.text = "Score: ${game.score}"

        // Set button text based on game state
        holder.btnPlay.text = if (game.isCompleted) "Play Again" else "Continue"

        holder.btnPlay.setOnClickListener {
            val intent = Intent(context, PlayCameraActivity::class.java)

            if (game.isCompleted) {
                // Restart completed game - pass category info for fresh start
                intent.putExtra(PlayCameraActivity.CATEGORY_KEY, game.category)
                intent.putExtra(PlayCameraActivity.CATEGORY_ENDPOINT, game.endpoint)
                // Don't pass game ID, so it creates a new game
            } else {
                // Continue unfinished game - pass game ID to load existing state
                intent.putExtra(PlayCameraActivity.GAME_ID, game.gameId)
            }

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = gameList.size
}