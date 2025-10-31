package com.itismob.group8.aslfingerspellingapp

import android.content.res.ColorStateList
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.media3.common.Player
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutDictionarywordBinding

//This and DictionaryWord holders are similar, but only one of them has the extra Edit and Delete buttons.
class DictionaryWordViewHolder (private val viewBinding: ItemLayoutDictionarywordBinding): RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(w: Word, onShowHideClick: () -> Unit) {
        viewBinding.wordName.text = w.wordName
        viewBinding.dWordDef.text = w.wordDef
        viewBinding.btnShowhide.tag = w.showInPlay
        if (viewBinding.btnShowhide.tag == "showing"){
            viewBinding.btnShowhide.setImageResource(R.drawable.hide)
        } else if (viewBinding.btnShowhide.tag == "hiding"){
            viewBinding.btnShowhide.setImageResource(R.drawable.show)
        }
        if (w.videoLink == null) {
            viewBinding.btnView.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.UnavailableGrey)))
            viewBinding.btnView.isEnabled = false
        } else {
            viewBinding.btnView.isEnabled = true
        }

        viewBinding.btnView.setOnClickListener {
            val c = viewBinding.root.context
            val vLink = w.videoLink
            // TODO("No existing View Video Activity yet for existing videos")
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
    }
}