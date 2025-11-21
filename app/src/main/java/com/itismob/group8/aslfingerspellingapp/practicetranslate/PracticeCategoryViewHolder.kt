package com.itismob.group8.aslfingerspellingapp.practicetranslate

import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.common.Category
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutCategoryBinding
class PracticeCategoryViewHolder (private val viewBinding: ItemLayoutCategoryBinding): RecyclerView.ViewHolder(viewBinding.root) {
    val btnCategory = viewBinding.llBtnCategory
    fun bindData(category: Category) {
        viewBinding.tvCategoryName.text = category.name
        viewBinding.ivBtnImage.setImageResource(category.image)
    }
}