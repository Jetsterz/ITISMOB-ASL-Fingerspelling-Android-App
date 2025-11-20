package com.itismob.group8.aslfingerspellingapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
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
        val txtCompletionInfo: TextView = view.findViewById(R.id.txtCompletionInfo)
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

        // Check if game is completed (reached final round)
        val isGameCompleted = game.currentRound >= game.totalRounds

        if (isGameCompleted) {
            // Game is completed - hide button and show completion status
            holder.txtRounds.text = "Completed!"
            holder.txtCompletionInfo.text = "Final Score: ${game.score}"
            holder.btnPlay.visibility = View.GONE
        } else {
            // Game is still in progress - show continue button
            holder.txtRounds.text = "Round: ${game.currentRound}/${game.totalRounds}"
            holder.txtCompletionInfo.text = "In Progress - Score: ${game.score}"
            holder.btnPlay.visibility = View.VISIBLE
            holder.btnPlay.text = "Continue"
            holder.btnPlay.isEnabled = true
            holder.btnPlay.isClickable = true
            holder.btnPlay.backgroundTintList = ContextCompat.getColorStateList(context, android.R.color.holo_blue_dark)
            holder.btnPlay.setTextColor(ContextCompat.getColor(context, android.R.color.white))

            holder.btnPlay.setOnClickListener {
                val intent = Intent(context, PlayCameraActivity::class.java)
                intent.putExtra(PlayCameraActivity.GAME_ID, game.gameId)
                context.startActivity(intent)
            }
        }

        holder.txtDate.text = game.getFormattedDate()
        holder.txtScore.text = "Score: ${game.score}"
    }

    override fun getItemCount(): Int = gameList.size
}