package com.itismob.group8.aslfingerspellingapp.wordlists.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.itismob.group8.aslfingerspellingapp.R

class WordDBHandler (c: Context)
: SQLiteOpenHelper(c, DATABASE_NAME, null, DATABASE_VERSION){
companion object {
    private const val DATABASE_VERSION = 1
    private const val DATABASE_NAME = "WordDB"

    const val DICTIO_WORDS_TABLE = "dictio_words_table"
    const val USER_WORD_TABLE = "user_words_table"

    const val WORD_ID = "_id"
    const val WORD_NAME = "word_name"
    const val WORD_DEF = "word_def"
    const val IS_HIDDEN = "is_hidden"
    const val CATEGORY = "category"

    @Volatile
    private var INSTANCE: WordDBHandler? = null

    fun getInstance(context: Context): WordDBHandler {
        return INSTANCE ?: synchronized(this) {
            val instance = WordDBHandler(context.applicationContext)
            INSTANCE = instance
            instance
        }
    }
}

override fun onCreate(db: SQLiteDatabase?) {
    val createUserDB = """
        CREATE TABLE IF NOT EXISTS $USER_WORD_TABLE (
            $WORD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $WORD_NAME TEXT,
            $WORD_DEF TEXT,
            $IS_HIDDEN BOOLEAN, 
            $CATEGORY TEXT
        )
""".trimIndent()
    db?.execSQL(createUserDB)
    val uv1 = cvMake("UserPlace", "A placeholder for the User Word list.", true, "PLACEHOLDER")
    val uv2 = cvMake("UserPlaceB", "Another placeholder for the User Word List.", false, "PLACEHOLDER")
    db?.insert(USER_WORD_TABLE, null, uv1)
    db?.insert(USER_WORD_TABLE, null, uv2)

    val createDictioDB = """
        CREATE TABLE IF NOT EXISTS $DICTIO_WORDS_TABLE (
            $WORD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $WORD_NAME TEXT,
            $WORD_DEF TEXT,
            $IS_HIDDEN BOOLEAN, 
            $CATEGORY TEXT
        )
""".trimIndent()
    db?.execSQL(createDictioDB)
    val cv1 = cvMake("DictioPlace", "A placeholder for the Dictionary Word list.", true, "PLACEHOLDER")
    val cv2 = cvMake("DictioPlaceB", "Another placeholder for the Dictionary Word List.", false, "PLACEHOLDER")
    db?.insert(DICTIO_WORDS_TABLE, null, cv1)
    db?.insert(DICTIO_WORDS_TABLE, null, cv2)
}

private fun cvMake(name: String, definition: String, showInPlay: Boolean, category: String) : ContentValues {
    val cv = ContentValues()
    cv.put(WORD_NAME, name)
    cv.put(WORD_DEF, definition)
    cv.put(IS_HIDDEN, showInPlay)
    cv.put(CATEGORY, category)
    return cv
}

override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    db!!.execSQL("DROP TABLE IF EXISTS $USER_WORD_TABLE")
    onCreate(db)
}

}