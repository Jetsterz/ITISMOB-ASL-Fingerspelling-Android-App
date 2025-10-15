package com.itismob.group8.aslfingerspellingapp

import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutCategoryBinding

class PlayCategoryViewHolder (private val viewBinding: ItemLayoutCategoryBinding): RecyclerView.ViewHolder(viewBinding.root) {

    public var btnCategoryPlay: Button = viewBinding.btnCategoryName
    fun bindData(category: String) {
        viewBinding.btnCategoryName.text = category
    }
}