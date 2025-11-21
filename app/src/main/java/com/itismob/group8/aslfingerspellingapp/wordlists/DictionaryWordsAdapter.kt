package com.itismob.group8.aslfingerspellingapp.wordlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.wordlists.Word
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutDictionarywordBinding

class DictionaryWordsAdapter(
    d: ArrayList<Word>,
    private val onShowHideClick: (position: Int) -> Unit
): RecyclerView.Adapter<DictionaryWordViewHolder>() {
    private val dat: ArrayList<Word> = d

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryWordViewHolder {
        val i: LayoutInflater = LayoutInflater.from(parent.context)
        val vb = ItemLayoutDictionarywordBinding.inflate(i, parent, false)

        val dv: DictionaryWordViewHolder = DictionaryWordViewHolder(vb)
        return dv
    }

    override fun onBindViewHolder(holder: DictionaryWordViewHolder, position: Int) {
        val w = dat[position]
        val viewholdcb = {
            onShowHideClick(position)
        }
        holder.bind(w, viewholdcb)
    }

    override fun getItemCount(): Int {
        return dat.size
    }
}