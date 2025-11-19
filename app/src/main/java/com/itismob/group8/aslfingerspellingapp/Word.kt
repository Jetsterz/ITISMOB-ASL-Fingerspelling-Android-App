package com.itismob.group8.aslfingerspellingapp

data class Word(
    var id : Int,
    var wordName: String,
    var wordDef: String,
    var videoLink: String?,
    var showInPlay : Boolean,
    var category : String
) {
    companion object {
        fun default() : Word {
            val defId = -1
            val defWordName = "PLACEHOLDER"
            val defWordDef = "PLACE HOLDING"
            val defVideoLink = "PLACE HELD"
            val defShowInPlay = false
            val defCategory = "PLACED"
            return Word(defId, defWordName, defWordDef, defVideoLink, defShowInPlay, defCategory)
        }
    }
}