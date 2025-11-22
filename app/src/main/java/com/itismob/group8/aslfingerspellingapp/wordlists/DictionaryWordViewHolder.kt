package com.itismob.group8.aslfingerspellingapp.wordlists

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.R
import com.itismob.group8.aslfingerspellingapp.wordlists.Word
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutDictionarywordBinding

//This and DictionaryWord holders are similar, but only one of them has the extra Edit and Delete buttons.
class DictionaryWordViewHolder (private val viewBinding: ItemLayoutDictionarywordBinding): RecyclerView.ViewHolder(viewBinding.root) {

    fun bind(w: Word, onShowHideClick: () -> Unit) {
        val c = viewBinding.root.context
        viewBinding.wordName.text = w.wordName
        viewBinding.dWordDef.text = w.wordDef
        viewBinding.btnShowhide.tag = w.showInPlay
        viewBinding.dCatView.text = w.category
        if (viewBinding.btnShowhide.tag == true){
            viewBinding.btnShowhide.setImageResource(R.drawable.hide)
        } else {
            viewBinding.btnShowhide.setImageResource(R.drawable.show)
        }

        viewBinding.btnPractice.setOnClickListener {
            val i = Intent(c, PracticeOneWordActivity::class.java)
            i.putExtra("id", w.id)
            i.putExtra("list", "DictioWordDatabase")
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
    }
}