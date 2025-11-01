package com.itismob.group8.aslfingerspellingapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutUserwordBinding

//This and DictionaryWord holders are similar, but only one of them has the extra Edit and Delete buttons.
class UserWordViewHolder (private val viewBinding: ItemLayoutUserwordBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(w: Word, onShowHideClick: () -> Unit, onDeleteClick: () -> Unit) {
        viewBinding.wordName.text = w.wordName
        viewBinding.uWordDef.text = w.wordDef
        viewBinding.btnShowhide.tag = w.showInPlay

        if (viewBinding.btnShowhide.tag == "showing"){
            viewBinding.btnShowhide.setImageResource(R.drawable.hide)
        } else if (viewBinding.btnShowhide.tag == "hiding"){
            viewBinding.btnShowhide.setImageResource(R.drawable.show)
        }
        if (w.videoLink != null) {
            viewBinding.btnViewedit.setImageResource(R.drawable.watchoreditvid)
        } else {
            viewBinding.btnViewedit.setImageResource(R.drawable.createvid)
        }

        viewBinding.btnViewedit.setOnClickListener {
            val c = viewBinding.root.context
            if (w.videoLink != null) {
                val vLink = w.videoLink
                // TODO("No existing View Video Activity yet for existing videos")
            } else {
                // TODO("No existing View Video Activity yet for nonexistent videos.")
            }
        }

        viewBinding.btnPractice.setOnClickListener {
            val c = viewBinding.root.context
            val vLink = w.videoLink
            // TODO("No existing Practice Word Activity yet.")
        }

        viewBinding.btnShowhide.setOnClickListener {
            if (viewBinding.btnShowhide.tag == "showing"){
                viewBinding.btnShowhide.setImageResource(R.drawable.show)
                viewBinding.btnShowhide.tag = "hiding"
            } else if (viewBinding.btnShowhide.tag == "hiding"){
                viewBinding.btnShowhide.setImageResource(R.drawable.hide)
                viewBinding.btnShowhide.tag = "showing"
            }
            onShowHideClick()
        }

        viewBinding.btnDelete.setOnClickListener {
            onDeleteClick()
        }
    }
}