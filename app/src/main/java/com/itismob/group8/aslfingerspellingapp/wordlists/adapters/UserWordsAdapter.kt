package com.itismob.group8.aslfingerspellingapp.wordlists.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.R
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutUserwordBinding
import com.itismob.group8.aslfingerspellingapp.wordlists.DisplayWordActivity
import com.itismob.group8.aslfingerspellingapp.wordlists.PracticeOneWordActivity
import com.itismob.group8.aslfingerspellingapp.wordlists.Word

class UserWordsAdapter(
    d: ArrayList<Word>,
    private val onShowHideClick: (position: Int) -> Unit,
    private val onDeleteClick: (position: Int) -> Unit
) : RecyclerView.Adapter<UserWordsAdapter.UserWordViewHolder>() {
    class UserWordViewHolder (private val viewBinding: ItemLayoutUserwordBinding): RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(w: Word, onShowHideClick: () -> Unit, onDeleteClick: () -> Unit) {
            val c = viewBinding.root.context
            viewBinding.wordName.text = w.wordName
            viewBinding.uWordDef.text = w.wordDef
            viewBinding.uCatView.text = w.category

            viewBinding.btnViewedit.setOnClickListener {
                val i = Intent(c, DisplayWordActivity::class.java)
                i.putExtra("list", "UserWordDatabase")
                i.putExtra("id", w.id)
                i.putExtra(DisplayWordActivity.WORD_TYPE_KEY, "u")
                c.startActivity(i)
            }

            viewBinding.btnPractice.setOnClickListener {
                val i = Intent(c, PracticeOneWordActivity::class.java)
                i.putExtra("id", w.id)
                i.putExtra("list", "UserWordDatabase")
                c.startActivity(i)
            }

            viewBinding.btnDelete.setOnClickListener {
                onDeleteClick()
            }
            if (w.wordDef.isNullOrEmpty()) {
                viewBinding.uWordDef.isGone = true
            }
        }
    }
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