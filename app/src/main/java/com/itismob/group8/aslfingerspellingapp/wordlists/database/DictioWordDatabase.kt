package com.itismob.group8.aslfingerspellingapp.wordlists.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.itismob.group8.aslfingerspellingapp.wordlists.database.WordDBHandler
import com.itismob.group8.aslfingerspellingapp.wordlists.Word

class DictioWordDatabase(c: Context) {
    val dbHelp = WordDBHandler.Companion.getInstance(c)

    fun addWord (w: Word) : Int {
        val db = dbHelp.writableDatabase

        val cv = ContentValues()
        cv.put(WordDBHandler.Companion.WORD_NAME, w.wordName)
        cv.put(WordDBHandler.Companion.WORD_DEF, w.wordDef)
        cv.put(WordDBHandler.Companion.WORD_LINK, w.videoLink)
        cv.put(WordDBHandler.Companion.IS_HIDDEN, w.showInPlay)
        cv.put(WordDBHandler.Companion.CATEGORY, w.category)

        val _id = db.insert(WordDBHandler.Companion.DICTIO_WORDS_TABLE, null, cv)
        return _id.toInt()
    }

    fun updateWord (w: Word) {
        val db = dbHelp.writableDatabase
        val where = "${WordDBHandler.Companion.WORD_ID} = ?"
        val args = arrayOf(w.id.toString())

        val cv = ContentValues()
        cv.put(WordDBHandler.Companion.WORD_NAME, w.wordName)
        cv.put(WordDBHandler.Companion.WORD_DEF, w.wordDef)
        cv.put(WordDBHandler.Companion.WORD_LINK, w.videoLink)
        cv.put(WordDBHandler.Companion.IS_HIDDEN, w.showInPlay)
        cv.put(WordDBHandler.Companion.CATEGORY, w.category)

        db.update(WordDBHandler.Companion.DICTIO_WORDS_TABLE, cv, where, args)
    }

    fun deleteWord (w: Word) {
        val db = dbHelp.writableDatabase
        val where = "${WordDBHandler.Companion.WORD_ID} = ?"
        val args = arrayOf(w.id.toString())

        db.delete(WordDBHandler.Companion.DICTIO_WORDS_TABLE, where, args)
    }

    fun getAllWords() : ArrayList<Word> {
        val r = ArrayList<Word>()
        val db = dbHelp.readableDatabase
        val c : Cursor = db.query(
            WordDBHandler.Companion.DICTIO_WORDS_TABLE,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (c.moveToNext()) {
            val toBool = when (c.getInt(c.getColumnIndexOrThrow(WordDBHandler.Companion.IS_HIDDEN))) {
                0 -> false else -> true
            }
            r.add(
                Word(
                    c.getInt(c.getColumnIndexOrThrow(WordDBHandler.Companion.WORD_ID)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.WORD_NAME)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.WORD_DEF)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.WORD_LINK)),
                    toBool,
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.CATEGORY)),
                )
            )
        }
        c.close()
        return r
    }

    fun getCategories() : ArrayList<String> {
        val r = ArrayList<String>()
        val db = dbHelp.readableDatabase
        val c : Cursor = db.query(
            WordDBHandler.Companion.DICTIO_WORDS_TABLE,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (c.moveToNext()) {
            r.add(c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.CATEGORY)))
        }

        c.close()
        return r
    }

    fun getWordsOfCategory(cat: String) : ArrayList<Word> {
        val r = ArrayList<Word>()
        val db = dbHelp.readableDatabase
        val c : Cursor = db.query(
            WordDBHandler.Companion.DICTIO_WORDS_TABLE,
            null,
            "${arrayOf(WordDBHandler.Companion.CATEGORY)} = ?",
            arrayOf(cat),
            null,
            null,
            null,
            null
        )
        while (c.moveToNext()) {
            val toBool = when (c.getInt(c.getColumnIndexOrThrow(WordDBHandler.Companion.IS_HIDDEN))) {
                0 -> false else -> true
            }
            r.add(
                Word(
                    c.getInt(c.getColumnIndexOrThrow(WordDBHandler.Companion.WORD_ID)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.WORD_NAME)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.WORD_DEF)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.WORD_LINK)),
                    toBool,
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.CATEGORY)),
                )
            )
        }

        c.close()
        return r
    }

    fun flipShowHide(w: Word) {
        val db = dbHelp.writableDatabase
        val where = "${WordDBHandler.Companion.WORD_ID} = ?"
        val args = arrayOf(w.id.toString())

        val cv = ContentValues()
        cv.put(WordDBHandler.Companion.IS_HIDDEN, !w.showInPlay)

        db.update(WordDBHandler.Companion.DICTIO_WORDS_TABLE, cv, where, args)
    }
}