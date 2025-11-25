package com.itismob.group8.aslfingerspellingapp.wordlists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutOneletterBinding

class ViewWordDemoAdapter(private val wordNameArray: CharArray) :
RecyclerView.Adapter<ViewWordDemoViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewWordDemoViewHolder {
        val i: LayoutInflater = LayoutInflater.from(parent.context)
        val vb = ItemLayoutOneletterBinding.inflate(i, parent, false)

        val vh: ViewWordDemoViewHolder = ViewWordDemoViewHolder(vb)
        return vh
    }

    override fun onBindViewHolder(holder: ViewWordDemoViewHolder, position: Int) {
        val p = wordNameArray[position]
        holder.bind(p)
    }

    override fun getItemCount(): Int {
        return wordNameArray.size
    }
}
