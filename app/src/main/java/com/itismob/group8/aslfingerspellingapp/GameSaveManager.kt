package com.itismob.group8.aslfingerspellingapp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.*

object GameSaveManager {
    private const val FILENAME = "saved_games.json"
    private val gson = Gson()

    fun saveGame(context: Context, game: PreviousGame) {
        val games = loadAllGames(context).toMutableList()

        // Remove existing game with same ID if it exists
        games.removeAll { it.gameId == game.gameId }

        // Add new game record
        games.add(game)

        // Save to file
        saveGamesToFile(context, games)
    }

    fun markGameAsCompleted(context: Context, gameId: String, finalScore: Int, startTime: String) {
        val games = loadAllGames(context).toMutableList()
        val gameIndex = games.indexOfFirst { it.gameId == gameId }

        if (gameIndex != -1) {
            val game = games[gameIndex]
            val completionTime = PreviousGame.calculateCompletionTime(startTime, PreviousGame.getCurrentDateTime())
            val completedGame = game.copy(
                isCompleted = true,
                completionDate = PreviousGame.getCurrentDateTime(),
                completionTime = completionTime,
                score = finalScore,
                currentRound = game.totalRounds // Set to final round
            )
            games[gameIndex] = completedGame
            saveGamesToFile(context, games)
        }
    }

    fun loadAllGames(context: Context): List<PreviousGame> {
        return try {
            val inputStream: FileInputStream = context.openFileInput(FILENAME)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<PreviousGame>>() {}.type
            gson.fromJson(jsonString, type) ?: emptyList()
        } catch (e: FileNotFoundException) {
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getGameById(context: Context, gameId: String): PreviousGame? {
        return loadAllGames(context).find { it.gameId == gameId }
    }

    fun deleteGame(context: Context, gameId: String) {
        val games = loadAllGames(context).toMutableList()
        games.removeAll { it.gameId == gameId }
        saveGamesToFile(context, games)
    }

    private fun saveGamesToFile(context: Context, games: List<PreviousGame>) {
        val jsonString = gson.toJson(games)
        context.openFileOutput(FILENAME, Context.MODE_PRIVATE).use { output ->
            output.write(jsonString.toByteArray())
        }
    }
}