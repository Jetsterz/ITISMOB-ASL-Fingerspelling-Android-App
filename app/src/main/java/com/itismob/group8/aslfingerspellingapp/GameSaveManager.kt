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
        val jsonString = gson.toJson(games)
        context.openFileOutput(FILENAME, Context.MODE_PRIVATE).use { output ->
            output.write(jsonString.toByteArray())
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

    fun deleteGame(context: Context, gameId: String) {
        val games = loadAllGames(context).toMutableList()
        games.removeAll { it.gameId == gameId }

        val jsonString = gson.toJson(games)
        context.openFileOutput(FILENAME, Context.MODE_PRIVATE).use { output ->
            output.write(jsonString.toByteArray())
        }
    }
}