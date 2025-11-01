package com.itismob.group8.aslfingerspellingapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutUserwordBinding

class UserWordsAdapter(d: ArrayList<Word>, private val onShowHideClick: (position: Int) -> Unit, private val onDeleteClick: (position: Int) -> Unit) : RecyclerView.Adapter<UserWordViewHolder>() {
    private val dat: ArrayList<Word> = d

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserWordViewHolder {
        val i: LayoutInflater = LayoutInflater.from(parent.context)
        val vb = ItemLayoutUserwordBinding.inflate(i, parent, false)

        val dv: UserWordViewHolder = UserWordViewHolder(vb)
        return dv
    }

    override fun onBindViewHolder(holder: UserWordViewHolder, position: Int) {
        val w = dat[position]
        val viewholdcb = {
            onShowHideClick(position)
        }
        val viewholdcbDel = {
            onDeleteClick(position)
        }
        holder.bind(w, viewholdcb, viewholdcbDel)
    }

    override fun getItemCount(): Int {
        return dat.size
    }
}