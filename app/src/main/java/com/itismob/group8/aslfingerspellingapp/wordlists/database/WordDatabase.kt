package com.itismob.group8.aslfingerspellingapp.wordlists.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.itismob.group8.aslfingerspellingapp.wordlists.Word

abstract class WordDatabase(c: Context){
    val dbHelp = WordDBHandler.Companion.getInstance(c)
    abstract val thisTab : String
    fun addWord (w: Word) : Int {
        val db = dbHelp.writableDatabase

        val cv = ContentValues()
        cv.put(WordDBHandler.Companion.WORD_NAME, w.wordName)
        cv.put(WordDBHandler.Companion.WORD_DEF, w.wordDef)
        cv.put(WordDBHandler.Companion.IS_HIDDEN, w.showInPlay)
        cv.put(WordDBHandler.Companion.CATEGORY, w.category)

        val _id = db.insert(thisTab, null, cv)
        return _id.toInt()
    }

    fun updateWord (w: Word) {
        val db = dbHelp.writableDatabase
        val where = "${WordDBHandler.Companion.WORD_ID} = ?"
        val args = arrayOf(w.id.toString())

        val cv = ContentValues()
        cv.put(WordDBHandler.Companion.WORD_NAME, w.wordName)
        cv.put(WordDBHandler.Companion.WORD_DEF, w.wordDef)
        cv.put(WordDBHandler.Companion.IS_HIDDEN, w.showInPlay)
        cv.put(WordDBHandler.Companion.CATEGORY, w.category)

        db.update(thisTab, cv, where, args)
    }

    fun deleteWord (w: Word) {
        val db = dbHelp.writableDatabase
        val where = "${WordDBHandler.Companion.WORD_ID} = ?"
        val args = arrayOf(w.id.toString())

        db.delete(thisTab, where, args)
    }

    fun getAllWords() : ArrayList<Word> {
        val r = ArrayList<Word>()
        val db = dbHelp.readableDatabase
        val c : Cursor = db.query(
            thisTab,
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
                    toBool,
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.CATEGORY))
                )
            )
        }
        c.close()
        return r
    }

    fun getShowingWords() : ArrayList<Word> {
        val r = ArrayList<Word>()
        val db = dbHelp.readableDatabase
        val c : Cursor = db.query(
            thisTab,
            null,
            "${WordDBHandler.IS_HIDDEN} = ?",
            arrayOf("0"),
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
                    toBool,
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.CATEGORY))
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
            true,
            thisTab,
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

    fun getShowingWordsOfCategory(cat: String) : ArrayList<Word> {
        val r = ArrayList<Word>()
        val db = dbHelp.readableDatabase
        val c : Cursor = db.query(
            thisTab,
            null,
            "${WordDBHandler.CATEGORY} = ? AND ${WordDBHandler.IS_HIDDEN} = ?",
            arrayOf(cat, "0"),
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
                    toBool,
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.CATEGORY))
                )
            )
        }

        c.close()
        return r
    }

    fun flipShowHide(w: Word) {
        val db = dbHelp.writableDatabase
        val where = "${WordDBHandler.WORD_ID} = ?"
        val args = arrayOf(w.id.toString())

        val cv = ContentValues()
        cv.put(WordDBHandler.IS_HIDDEN, !w.showInPlay)

        db.update(thisTab, cv, where, args)
    }

    fun findWordByID(id: Int) : Word? {
        val db = dbHelp.readableDatabase
        val where = "${WordDBHandler.WORD_ID} = ?"
        val arg = arrayOf(id.toString())
        var w: Word? = null

        val c : Cursor = db.query(
            thisTab,
            null,
            where,
            arg,
            null,
            null,
            null,
            null
        )
        c.use { _ ->
            if (c.moveToFirst()) {
                val toBool = when (c.getInt(c.getColumnIndexOrThrow(WordDBHandler.IS_HIDDEN))) {
                    0 -> false else -> true
                }
                w = Word(
                    c.getInt(c.getColumnIndexOrThrow(WordDBHandler.Companion.WORD_ID)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.WORD_NAME)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.WORD_DEF)),
                    toBool,
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.Companion.CATEGORY))
                )
            }
        }
        return w
    }
}