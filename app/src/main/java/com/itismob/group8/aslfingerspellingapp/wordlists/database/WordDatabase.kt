package com.itismob.group8.aslfingerspellingapp.wordlists.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.itismob.group8.aslfingerspellingapp.wordlists.Word

/**
* WordDatabase is the base class from which subsequent databases UserWordDatabase and DictioWordDatabase inherit from.
* @param c: Context of the activity/fragment this is called in.
* */
abstract class WordDatabase(c: Context){
    val dbHelp = WordDBHandler.getInstance(c)
    abstract val thisTab : String
    fun addWord (w: Word) : Int {
        val db = dbHelp.writableDatabase

        val cv = ContentValues()
        cv.put(WordDBHandler.WORD_NAME, w.wordName)
        cv.put(WordDBHandler.WORD_DEF, w.wordDef)
        cv.put(WordDBHandler.IS_HIDDEN, w.showInPlay)
        cv.put(WordDBHandler.CATEGORY, w.category)

        val _id = db.insertWithOnConflict(thisTab, null, cv, SQLiteDatabase.CONFLICT_IGNORE)
        return _id.toInt()
    }

    fun updateWord (w: Word) {
        val db = dbHelp.writableDatabase
        val where = "${WordDBHandler.WORD_ID} = ?"
        val args = arrayOf(w.id.toString())

        val cv = ContentValues()
        cv.put(WordDBHandler.WORD_NAME, w.wordName)
        cv.put(WordDBHandler.WORD_DEF, w.wordDef)
        cv.put(WordDBHandler.IS_HIDDEN, w.showInPlay)
        cv.put(WordDBHandler.CATEGORY, w.category)

        db.update(thisTab, cv, where, args)
    }

    fun deleteWord (w: Word) {
        val db = dbHelp.writableDatabase
        val where = "${WordDBHandler.WORD_ID} = ?"
        val args = arrayOf(w.id.toString())

        db.delete(thisTab, where, args)
    }

    fun getAllWords() : ArrayList<Word> {
        val r = ArrayList<Word>()
        val db = dbHelp.readableDatabase
        val sort = "${WordDBHandler.WORD_NAME} ASC"
        val c : Cursor = db.query(
            thisTab,
            null,
            null,
            null,
            null,
            null,
            sort,
            null
        )
        while (c.moveToNext()) {
            val toBool = when (c.getInt(c.getColumnIndexOrThrow(WordDBHandler.IS_HIDDEN))) {
                0 -> false else -> true
            }
            r.add(
                Word(
                    c.getInt(c.getColumnIndexOrThrow(WordDBHandler.WORD_ID)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_NAME)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_DEF)),
                    toBool,
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.CATEGORY))
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
            val toBool = when (c.getInt(c.getColumnIndexOrThrow(WordDBHandler.IS_HIDDEN))) {
                0 -> false else -> true
            }
            r.add(
                Word(
                    c.getInt(c.getColumnIndexOrThrow(WordDBHandler.WORD_ID)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_NAME)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_DEF)),
                    toBool,
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.CATEGORY))
                )
            )
        }
        c.close()
        return r
    }

    fun getCategories() : ArrayList<String> {
        val r = HashSet<String>()
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
            r.add(c.getString(c.getColumnIndexOrThrow(WordDBHandler.CATEGORY)))
        }

        c.close()
        return ArrayList(r)
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
            val toBool = when (c.getInt(c.getColumnIndexOrThrow(WordDBHandler.IS_HIDDEN))) {
                0 -> false else -> true
            }
            r.add(
                Word(
                    c.getInt(c.getColumnIndexOrThrow(WordDBHandler.WORD_ID)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_NAME)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_DEF)),
                    toBool,
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.CATEGORY))
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
                    c.getInt(c.getColumnIndexOrThrow(WordDBHandler.WORD_ID)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_NAME)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_DEF)),
                    toBool,
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.CATEGORY))
                )
            }
        }
        return w
    }

    fun findWordByNameAndCat(name: String?, cat: String?) : ArrayList<Word> {
        if (name == null && cat == null) {
            return getAllWords()
        }
        val db = dbHelp.readableDatabase
        val selectionParts = mutableListOf<String>()
        val args = mutableListOf<String>()

        if (name != null) {
            selectionParts.add("${WordDBHandler.WORD_NAME} = ?")
            args.add(name)
        }

        if (cat != null) {
            selectionParts.add("${WordDBHandler.CATEGORY} = ?")
            args.add(cat)
        }
        var where = selectionParts.joinToString(" AND ")
        var wList = ArrayList<Word>()
        val sort = "${WordDBHandler.WORD_NAME} ASC"

        val c : Cursor = db.query(
            thisTab,
            null,
            where,
            args.toTypedArray(),
            null,
            null,
            sort,
            null
        )
        while (c.moveToNext()) {
            val toBool = when (c.getInt(c.getColumnIndexOrThrow(WordDBHandler.IS_HIDDEN))) {
                0 -> false else -> true
            }
            wList.add(
                Word(
                    c.getInt(c.getColumnIndexOrThrow(WordDBHandler.WORD_ID)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_NAME)),
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.WORD_DEF)),
                    toBool,
                    c.getString(c.getColumnIndexOrThrow(WordDBHandler.CATEGORY))
                )
            )
        }
        return wList
    }
}