package com.itismob.group8.aslfingerspellingapp.common

class Category(val name: String, val image: Int, val endpoint: String, val api: Int) {

    companion object {
        const val DATAMUSE_API = 1
        const val LIST_OF_NAMES_DATASET = 2
    }
}