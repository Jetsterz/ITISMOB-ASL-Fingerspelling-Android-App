package com.itismob.group8.aslfingerspellingapp.wordlists

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.R
import com.itismob.group8.aslfingerspellingapp.wordlists.Word
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutUserwordBinding

//This and DictionaryWord holders are similar, but only one of them has the extra Edit and Delete buttons.
class UserWordViewHolder (private val viewBinding: ItemLayoutUserwordBinding): RecyclerView.ViewHolder(viewBinding.root) {
    fun bind(w: Word, onShowHideClick: () -> Unit, onDeleteClick: () -> Unit) {
        val c = viewBinding.root.context
        viewBinding.wordName.text = w.wordName
        viewBinding.uWordDef.text = w.wordDef
        viewBinding.btnShowhide.tag = w.showInPlay
        viewBinding.uCatView.text = w.category

        if (viewBinding.btnShowhide.tag == true){
            viewBinding.btnShowhide.setImageResource(R.drawable.hide)
        } else {
            viewBinding.btnShowhide.setImageResource(R.drawable.show)
        }
        if (w.videoLink != null) {
            viewBinding.btnViewedit.setImageResource(R.drawable.watchoreditvid)
        } else {
            viewBinding.btnViewedit.setImageResource(R.drawable.edit)
        }

        viewBinding.btnViewedit.setOnClickListener {
            val i = Intent(c, DisplayWordActivity::class.java)
            i.putExtra("list", "UserWordDatabase")
            i.putExtra("id", w.id)
            // tells DisplayWordActivity that a video link exists if true
            if (w.videoLink != null) {
                i.putExtra("video?", true)
                c.startActivity(i)
            }
            // or... you know... the opposite of doing that
            else {
                i.putExtra("video?", false)
                c.startActivity(i)
            }
        }

        viewBinding.btnPractice.setOnClickListener {
            val i = Intent(c, PracticeOneWordActivity::class.java)
            i.putExtra("id", w.id)
            i.putExtra("list", "UserWordDatabase")
            c.startActivity(i)
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