package com.itismob.group8.aslfingerspellingapp

import android.widget.ImageButton
import android.widget.TextView
import androidx.media3.common.Player
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutDictionarywordBinding

class DictionaryWordViewHolder (private val viewBinding: ItemLayoutDictionarywordBinding): RecyclerView.ViewHolder(viewBinding.root) {
    public var dWordVideo: androidx.media3.ui.PlayerView = viewBinding.dWordVideo
    public var dWordName: TextView = viewBinding.wordName
    public var btn_practice: ImageButton = viewBinding.btnPractice
    public var btn_showhide: ImageButton = viewBinding.btnShowhide

    fun bindWord(word: String) {
        this.dWordName.text = word
    }

    fun bindVideo(player: Player) {
        //video bind, somehow
    }
}