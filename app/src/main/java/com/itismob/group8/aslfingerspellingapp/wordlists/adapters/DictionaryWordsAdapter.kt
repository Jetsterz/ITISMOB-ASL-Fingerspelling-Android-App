package com.itismob.group8.aslfingerspellingapp.wordlists.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.R
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutDictionarywordBinding
import com.itismob.group8.aslfingerspellingapp.wordlists.DisplayWordActivity
import com.itismob.group8.aslfingerspellingapp.wordlists.PracticeOneWordActivity
import com.itismob.group8.aslfingerspellingapp.wordlists.Word

class DictionaryWordsAdapter(
    d: ArrayList<Word>,
    private val onShowHideClick: (position: Int) -> Unit
): RecyclerView.Adapter<DictionaryWordsAdapter.DictionaryWordViewHolder>() {
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

            viewBinding.btnView.setOnClickListener {
                val i = Intent(c, DisplayWordActivity::class.java)
                i.putExtra("list", "DictioWordDatabase")
                i.putExtra("id", w.id)
                c.startActivity(i)
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