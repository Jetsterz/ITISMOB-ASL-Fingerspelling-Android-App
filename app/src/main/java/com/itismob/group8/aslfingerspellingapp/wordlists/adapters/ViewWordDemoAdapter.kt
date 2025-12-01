package com.itismob.group8.aslfingerspellingapp.wordlists.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.R
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutOneletterBinding

class ViewWordDemoAdapter(private val wordNameArray: CharArray) :
RecyclerView.Adapter<ViewWordDemoAdapter.ViewWordDemoViewHolder>(){
    class ViewWordDemoViewHolder(private val b: ItemLayoutOneletterBinding) :
        RecyclerView.ViewHolder(b.root) {
        companion object {
            val mapOfSigns = mapOf(
                'A' to R.drawable.a_sign,
                'B' to R.drawable.b_sign,
                'C' to R.drawable.c_sign,
                'D' to R.drawable.d_sign,
                'E' to R.drawable.e_sign,
                'F' to R.drawable.f_sign,
                'G' to R.drawable.g_sign,
                'H' to R.drawable.h_sign,
                'I' to R.drawable.i_sign,
                'J' to R.drawable.j_sign,
                'K' to R.drawable.k_sign,
                'L' to R.drawable.l_sign,
                'M' to R.drawable.m_sign,
                'N' to R.drawable.n_sign,
                'O' to R.drawable.o_sign,
                'P' to R.drawable.p_sign,
                'Q' to R.drawable.q_sign,
                'R' to R.drawable.r_sign,
                'S' to R.drawable.s_sign,
                'T' to R.drawable.t_sign,
                'U' to R.drawable.u_sign,
                'V' to R.drawable.v_sign,
                'W' to R.drawable.w_sign,
                'X' to R.drawable.x_sign,
                'Y' to R.drawable.y_sign,
                'Z' to R.drawable.z_sign
            )
        }
        fun bind(letter: Char) {
            val img = mapOfSigns[letter.uppercaseChar()]
            img?.let { imgID ->
                b.letterSign.setImageResource(imgID)
            }
            "Letter: ${letter.uppercaseChar()}".also { b.letterName.text = it }
        }


    }
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