package com.itismob.group8.aslfingerspellingapp

import java.util.*

data class PreviousGame(
    val imageResId: Int,
    val category: String,
    val rounds: Int,
    val date: String,
    val score: Int = 0,
    val isCompleted: Boolean = false,
    val gameId: String = ""
)