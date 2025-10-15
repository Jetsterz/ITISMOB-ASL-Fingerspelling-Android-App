package com.itismob.group8.aslfingerspellingapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutCategoryBinding

class PlayCategoryAdapter(private val playCameraActivityLauncher:
                              ActivityResultLauncher<Intent>,
                              private val categoryActivity: PlayCategoryActivity): RecyclerView.Adapter<PlayCategoryViewHolder>() {
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
    ): PlayCategoryViewHolder {
        val itemViewBinding = ItemLayoutCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        val viewHolder = PlayCategoryViewHolder(itemViewBinding)
        return viewHolder
    }

    override fun onBindViewHolder(
        holder: PlayCategoryViewHolder,
        position: Int
    ) {
        holder.bindData(categoryList[position])

        holder.btnCategoryPlay.setOnClickListener {
            var categoryIntent = Intent(holder.itemView.context,
                PlayCameraActivity::class.java)

            categoryIntent.putExtra(PlayCameraActivity.CATEGORY_KEY,
                categoryList[position])

            this.playCameraActivityLauncher.launch(categoryIntent)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}