package com.itismob.group8.aslfingerspellingapp

import java.util.*

data class PreviousGame(
    val imageResId: Int,
    val category: String,
    val currentRound: Int,
    val totalRounds: Int,
    val date: String,
    val score: Int = 0,
    val isCompleted: Boolean = false,
    val gameId: String = "",
    val endpoint: String = "",
    val wordsList: List<String> = emptyList(), // Store words for continuity
    val currentWordIndex: Int = 0 // Track which word we're on
)