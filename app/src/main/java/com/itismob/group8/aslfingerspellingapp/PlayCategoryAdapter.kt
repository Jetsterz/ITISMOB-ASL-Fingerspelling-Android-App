package com.itismob.group8.aslfingerspellingapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.common.Category
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutCategoryBinding

class PlayCategoryAdapter(
    private val playCameraActivityLauncher: ActivityResultLauncher<Intent>,
    private val categoryActivity: PlayCategoryActivity
) : RecyclerView.Adapter<PlayCategoryViewHolder>() {

    private var categoryList: ArrayList<Category> = arrayListOf(
        Category("Animals", R.drawable.ctgry_animals,
            "words?rel_gen=animal&topics=living,organism,domestic,life,earth", 1),
        Category("Food", R.drawable.ctgry_food, "words?rel_gen=food", 1),
        Category("Objects", R.drawable.ctgry_objects,
            "words?rel_gen=object&topics=clothing,furniture,materials,nonliving,concrete", 1),
        Category("Names", R.drawable.ctgry_names, "na", 2),
        Category("Short Words", R.drawable.ctgry_short_words, "words?sp=?????", 1),
        Category("Long Words", R.drawable.ctgry_long_words, "words?sp=??????????????", 1),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayCategoryViewHolder {
        val itemViewBinding = ItemLayoutCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return PlayCategoryViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: PlayCategoryViewHolder, position: Int) {
        holder.bindData(categoryList[position])

        holder.btnCategoryPlay.setOnClickListener {
            val categoryIntent = Intent(holder.itemView.context, PlayCameraActivity::class.java)
            categoryIntent.putExtra(PlayCameraActivity.CATEGORY_KEY, categoryList[position].name)
            categoryIntent.putExtra(PlayCameraActivity.CATEGORY_ENDPOINT, categoryList[position].endpoint)
            this.playCameraActivityLauncher.launch(categoryIntent)
        }
    }

    override fun getItemCount(): Int = categoryList.size
}