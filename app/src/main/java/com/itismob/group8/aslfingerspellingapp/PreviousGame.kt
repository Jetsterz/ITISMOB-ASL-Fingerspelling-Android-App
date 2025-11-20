package com.itismob.group8.aslfingerspellingapp

import java.text.SimpleDateFormat
import java.util.*

data class PreviousGame(
    val imageResId: Int,
    val category: String,
    val currentRound: Int,
    val totalRounds: Int,
    val date: String,
    val score: Int = 0,
    val gameId: String = "",
    val endpoint: String = ""
) {
    fun getFormattedDate(): String {
        return "Started: $date"
    }

    companion object {
        fun getCurrentDateTime(): String {
            val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
            return formatter.format(Date())
        }
    }
}