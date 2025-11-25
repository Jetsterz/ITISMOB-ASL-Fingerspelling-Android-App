package com.itismob.group8.aslfingerspellingapp.wordlists

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutOneminiletterBinding

class MiniDemoAdapter(private val wordNameArray: CharArray) :
    RecyclerView.Adapter<MiniDemoAdapter.MiniDemoViewHolder>() {
    inner class MiniDemoViewHolder(private val b: ItemLayoutOneminiletterBinding) : RecyclerView.ViewHolder(b.root){
        val arrayOfSigns = ViewWordDemoViewHolder.arrayOfSigns
        fun bind(l: Char){
            val thisImg = arrayOfSigns[l.uppercaseChar()]
            thisImg?.let { imgimg ->
                b.miniletter.setImageResource(imgimg)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniDemoViewHolder {
        val i: LayoutInflater = LayoutInflater.from(parent.context)
        val vb = ItemLayoutOneminiletterBinding.inflate(i, parent, false)

        val vh: MiniDemoViewHolder = MiniDemoViewHolder(vb)
        return vh
    }

    override fun onBindViewHolder(holder: MiniDemoViewHolder, position: Int) {
        val p = wordNameArray[position]
        holder.bind(p)
    }

    override fun getItemCount(): Int {
        return wordNameArray.size
    }
}