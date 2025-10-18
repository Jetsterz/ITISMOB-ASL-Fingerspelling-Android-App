package com.itismob.group8.aslfingerspellingapp

import android.widget.ImageButton
import android.widget.TextView
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutUserwordBinding

//This and DictionaryWord holders are similar, but only one of them has the extra Edit and Delete buttons.
class UserWordViewHolder (private val viewBinding: ItemLayoutUserwordBinding): RecyclerView.ViewHolder(viewBinding.root) {
    public var uWordName: TextView = viewBinding.wordName
    public var btn_practice: ImageButton = viewBinding.btnPractice
    public var btn_showhide: ImageButton = viewBinding.btnShowhide
    public var btn_edit: ImageButton = viewBinding.btnEdit
    public var btn_delete: ImageButton = viewBinding.btnDelete
}