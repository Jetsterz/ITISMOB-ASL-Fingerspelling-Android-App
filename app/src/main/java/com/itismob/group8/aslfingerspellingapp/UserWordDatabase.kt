package com.itismob.group8.aslfingerspellingapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class UserWordDatabase(c: Context) {

    val dbHelp = WordDBHandler.getInstance(c)

    fun addWord (w: Word) : Int {
        val db = dbHelp.writableDatabase

        val cv = ContentValues()
        cv.put(WordDBHandler.WORD_NAME, w.wordName)
        cv.put(WordDBHandler.WORD_DEF, w.wordDef)
        cv.put(WordDBHandler.WORD_LINK, w.videoLink)
        cv.put(WordDBHandler.IS_HIDDEN, w.showInPlay)
        cv.put(WordDBHandler.CATEGORY, w.category)

        val _id = db.insert(WordDBHandler.USER_WORD_TABLE, null, cv)
        return _id.toInt()
    }

    fun updateWord (w: Word) {
        val db = dbHelp.writableDatabase
        val where = "${WordDBHandler.WORD_ID} = ?"
        val args = arrayOf(w.id.toString())

        val cv = ContentValues()
        cv.put(WordDBHandler.WORD_NAME, w.wordName)
        cv.put(WordDBHandler.WORD_DEF, w.wordDef)
        cv.put(WordDBHandler.WORD_LINK, w.videoLink)
        cv.put(WordDBHandler.IS_HIDDEN, w.showInPlay)
        cv.put(WordDBHandler.CATEGORY, w.category)

        db.update(WordDBHandler.USER_WORD_TABLE, cv, where, args)
    }

    fun deleteWord (w: Word) {
        val db = dbHelp.writableDatabase
        val where = "${WordDBHandler.WORD_ID} = ?"
        val args = arrayOf(w.id.toString())

        db.delete(WordDBHandler.USER_WORD_TABLE, where, args)
    }

    fun getAllWords() : ArrayList<Word> {
        val r = ArrayList<Word>()
        val db = dbHelp.readableDatabase
        val c : Cursor = db.query(
            WordDBHandler.USER_WORD_TABLE,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (c.moveToNext()) {
            val toBool = when (c.getInt(c.getColumnIndexOrThrow(WordDBHandler.IS_HIDDEN))) {
                0 -> false else -> true
            }
            r.add(Word(
                c.getInt(c.getColumnIndexOrThrow(WordDBHandler.WORD_ID)),
                c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_NAME)),
                c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_DEF)),
                c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_LINK)),
                toBool,
                c.getString(c.getColumnIndexOrThrow(WordDBHandler.CATEGORY)),
            ))
        }
        c.close()
        return r
    }

    fun getCategories() : ArrayList<String> {
        val r = ArrayList<String>()
        val db = dbHelp.readableDatabase
        val colToReturn = arrayOf(WordDBHandler.CATEGORY)
        val c : Cursor = db.query(
            WordDBHandler.USER_WORD_TABLE,
            colToReturn,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (c.moveToNext()) {
            r.add(c.getString(c.getColumnIndexOrThrow(WordDBHandler.CATEGORY)))
        }

        c.close()
        return r
    }

    fun getWordsOfCategory(cat: String) : ArrayList<Word> {
        val r = ArrayList<Word>()
        val db = dbHelp.readableDatabase
        val c : Cursor = db.query(
            WordDBHandler.USER_WORD_TABLE,
            null,
            "${arrayOf(WordDBHandler.CATEGORY)} = ?",
            arrayOf(cat),
            null,
            null,
            null,
            null
        )
        while (c.moveToNext()) {
            val toBool = when (c.getInt(c.getColumnIndexOrThrow(WordDBHandler.IS_HIDDEN))) {
                0 -> false else -> true
            }
            r.add(Word(
                c.getInt(c.getColumnIndexOrThrow(WordDBHandler.WORD_ID)),
                c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_NAME)),
                c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_DEF)),
                c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_LINK)),
                toBool,
                c.getString(c.getColumnIndexOrThrow(WordDBHandler.CATEGORY)),
            ))
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

        db.update(WordDBHandler.USER_WORD_TABLE, cv, where, args)
    }
}