package com.itismob.group8.aslfingerspellingapp

import android.content.Intent
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
        val c = viewBinding.root.context
        viewBinding.wordName.text = w.wordName
        viewBinding.uWordDef.text = w.wordDef
        viewBinding.btnShowhide.tag = w.showInPlay

        if (viewBinding.btnShowhide.tag == true){
            viewBinding.btnShowhide.setImageResource(R.drawable.hide)
        } else {
            viewBinding.btnShowhide.setImageResource(R.drawable.show)
        }
        if (w.videoLink != null) {
            viewBinding.btnViewedit.setImageResource(R.drawable.watchoreditvid)
        } else {
            viewBinding.btnViewedit.setImageResource(R.drawable.createvid)
        }

        viewBinding.btnViewedit.setOnClickListener {
            val i = Intent(c, DisplayWordActivity::class.java)
            i.putExtra("wordName", w.wordName)
            i.putExtra("wordDef", w.wordDef)
            i.putExtra("videoLink", w.videoLink)
            if (w.videoLink != null) {
                i.putExtra("video?", true)
                c.startActivity(i)
            } else {
                i.putExtra("video?", false)
                c.startActivity(i)
            }
        }

        viewBinding.btnPractice.setOnClickListener {
            val vLink = w.videoLink
            // TODO("No existing Practice Word Activity yet.")
        }

        viewBinding.btnShowhide.setOnClickListener {
            if (viewBinding.btnShowhide.tag == true){
                viewBinding.btnShowhide.setImageResource(R.drawable.show)
                viewBinding.btnShowhide.tag = false
            } else {
                viewBinding.btnShowhide.setImageResource(R.drawable.hide)
                viewBinding.btnShowhide.tag = true
            }
            onShowHideClick()
        }

        viewBinding.btnDelete.setOnClickListener {
            onDeleteClick()
        }
    }
}