package com.itismob.group8.aslfingerspellingapp

import java.text.SimpleDateFormat
import java.util.*

data class PreviousGame(
    val imageResId: Int,
    val category: String,
    val currentRound: Int,
    val totalRounds: Int,
    val date: String, // Start date
    val completionDate: String? = null, // Completion date (null if not completed)
    val completionTime: String? = null, // Time taken to complete (e.g., "5 min 30 sec")
    val score: Int = 0,
    val isCompleted: Boolean = false,
    val gameId: String = "",
    val endpoint: String = ""
) {
    fun getFormattedDate(): String {
        return if (isCompleted && completionDate != null) {
            "Completed: $completionDate"
        } else {
            "Started: $date"
        }
    }

    fun getCompletionInfo(): String {
        return if (isCompleted && completionTime != null) {
            "Time: $completionTime"
        } else if (isCompleted) {
            "Completed"
        } else {
            "In Progress"
        }
    }

    companion object {
        fun getCurrentDateTime(): String {
            val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
            return formatter.format(Date())
        }

        fun calculateCompletionTime(startTime: String, endTime: String): String {
            return try {
                val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                val start = formatter.parse(startTime)
                val end = formatter.parse(endTime)

                val diff = end.time - start.time
                val minutes = diff / (60 * 1000)
                val seconds = (diff % (60 * 1000)) / 1000

                "${minutes} min ${seconds} sec"
            } catch (e: Exception) {
                "Unknown"
            }
        }
    }
}