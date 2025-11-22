package com.itismob.group8.aslfingerspellingapp.wordlists.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.itismob.group8.aslfingerspellingapp.wordlists.database.WordDBHandler
import com.itismob.group8.aslfingerspellingapp.wordlists.Word

class DictioWordDatabase(c: Context) : WordDatabase(c) {
    override val thisTab = WordDBHandler.DICTIO_WORDS_TABLE
}