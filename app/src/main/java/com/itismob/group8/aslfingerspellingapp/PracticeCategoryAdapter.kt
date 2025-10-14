package com.itismob.group8.aslfingerspellingapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutCategoryBinding

class PracticeCategoryAdapter(private val practiceCameraActivityLauncher:
                              ActivityResultLauncher<Intent>,
    private val categoryActivity: PracticeCategoryActivity): RecyclerView.Adapter<PracticeCategoryViewHolder>() {
    private val categoryList: ArrayList<String> = arrayListOf(
        "Animals",
        "Food",
        "Objects",
        "Brands",
        "Names",
        "Long Words",
        "Short Words"
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PracticeCategoryViewHolder {
        val itemViewBinding = ItemLayoutCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        val viewHolder = PracticeCategoryViewHolder(itemViewBinding)
        return viewHolder
    }

    override fun onBindViewHolder(
        holder: PracticeCategoryViewHolder,
        position: Int
    ) {
        holder.bindData(categoryList[position])

        holder.btnCategory.setOnClickListener {
            var categoryIntent = Intent(holder.itemView.context,
                PracticeCameraActivity::class.java)

            categoryIntent.putExtra(PracticeCameraActivity.CATEGORY_KEY,
                categoryList[position])

            this.practiceCameraActivityLauncher.launch(categoryIntent)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}