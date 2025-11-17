package com.itismob.group8.aslfingerspellingapp

import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.common.Category
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutCategoryBinding

class PlayCategoryViewHolder(private val viewBinding: ItemLayoutCategoryBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    public var btnCategoryPlay = viewBinding.llBtnCategory

    fun bindData(category: Category) {
        viewBinding.tvCategoryName.text = category.name
        viewBinding.ivBtnImage.setImageResource(category.image)
    }
}