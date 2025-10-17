package com.itismob.group8.aslfingerspellingapp

import android.widget.ImageButton
import android.widget.TextView
import androidx.media3.common.Player
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutDictionarywordBinding

//This and DictionaryWord holders are similar, but only one of them has the extra Edit and Delete buttons.
class DictionaryWordViewHolder (private val viewBinding: ItemLayoutDictionarywordBinding): RecyclerView.ViewHolder(viewBinding.root) {
    public var dWordName: TextView = viewBinding.wordName
    public var btn_practice: ImageButton = viewBinding.btnPractice
    public var btn_showhide: ImageButton = viewBinding.btnShowhide
}