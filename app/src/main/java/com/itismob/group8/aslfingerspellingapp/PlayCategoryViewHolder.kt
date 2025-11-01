package com.itismob.group8.aslfingerspellingapp

import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutCategoryBinding

class PlayCategoryViewHolder (private val viewBinding: ItemLayoutCategoryBinding): RecyclerView.ViewHolder(viewBinding.root) {

    public var btnCategoryPlay = viewBinding.llBtnCategory
    fun bindData(category: String) {
        viewBinding.tvCategoryName.text = category
    }
}