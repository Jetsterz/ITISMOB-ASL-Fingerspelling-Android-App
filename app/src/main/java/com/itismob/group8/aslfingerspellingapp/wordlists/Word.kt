package com.itismob.group8.aslfingerspellingapp.wordlists

data class Word(
    var id : Int,
    var wordName: String,
    var wordDef: String,
    var showInPlay : Boolean,
    var category : String
) {
    companion object {
        fun default() : Word {
            val defId = -1
            val defWordName = "PLACEHOLDER"
            val defWordDef = "PLACE HOLDING"
            val defShowInPlay = false
            val defCategory = "PLACED"
            return Word(defId, defWordName, defWordDef, defShowInPlay, defCategory)
        }
    }
}