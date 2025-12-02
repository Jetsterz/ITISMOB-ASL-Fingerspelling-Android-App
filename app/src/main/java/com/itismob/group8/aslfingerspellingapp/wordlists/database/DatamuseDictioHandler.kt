package com.itismob.group8.aslfingerspellingapp.wordlists.database

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.itismob.group8.aslfingerspellingapp.dataclasses.WordsData
import com.itismob.group8.aslfingerspellingapp.retrofit.DatamuseRetrofitHelper
import com.itismob.group8.aslfingerspellingapp.wordlists.Word
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.text.first
import kotlin.text.isNotBlank
import kotlin.text.isNullOrEmpty

class DatamuseDictioHandler(c: Context){
    val db = DictioWordDatabase(c)
    private lateinit var actualWords : ArrayList<Word>
    suspend fun grepWords(cat: String) {
        withContext(Dispatchers.IO) {
            try {
                val res = db.getShowingWordsOfCategory(cat)
                if (res.count() == 0) {
                    val wordListOnly = DatamuseRetrofitHelper.datamuseInterface.getWordsOnly(cat)
                    for (w in wordListOnly) {
                        val wordName = w.word.trim()
                        if (wordName.isBlank()) continue
                        val newWord = Word(-1, wordName, null, false, cat)
                        db.addWord(newWord)
                    }
                }
            } catch (e: Exception) {
                Log.e("HANDLER", "Error in grepWords for '$cat'", e)
            }
        }
    }
}