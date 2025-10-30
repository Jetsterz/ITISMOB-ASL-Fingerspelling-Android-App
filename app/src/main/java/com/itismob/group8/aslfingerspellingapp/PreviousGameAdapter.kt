package com.itismob.group8.aslfingerspellingapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        holder.txtRounds.text = "Rounds: ${game.rounds}"
        holder.txtDate.text = "Date: ${game.date}"

        holder.btnPlay.setOnClickListener {
            Toast.makeText(context, "Playing ${game.category} game!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = gameList.size
}
