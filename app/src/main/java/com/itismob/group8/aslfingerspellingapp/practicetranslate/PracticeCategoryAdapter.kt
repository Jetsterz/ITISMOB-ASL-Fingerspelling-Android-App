package com.itismob.group8.aslfingerspellingapp.practicetranslate

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.common.Category
import com.itismob.group8.aslfingerspellingapp.R
import com.itismob.group8.aslfingerspellingapp.databinding.ItemLayoutCategoryBinding

class PracticeCategoryAdapter(private val practiceCameraActivityLauncher:
                              ActivityResultLauncher<Intent>,
    private val categoryActivity: PracticeCategoryActivity): RecyclerView.Adapter<PracticeCategoryViewHolder>() {
    private var categoryList: ArrayList<Category> = arrayListOf(

        Category(
            "Animals",
            R.drawable.ctgry_animals,
            "words?rel_gen=animal&topics=living,organism,domestic,life,earth",
            Category.Companion.DATAMUSE_API
        ),
        Category(
            "Food",
            R.drawable.ctgry_food,
            "words?rel_gen=food",
            Category.Companion.DATAMUSE_API
        ),
        Category(
            "Objects",
            R.drawable.ctgry_objects,
            "words?rel_gen=object&topics=clothing,furniture,materials,nonliving,concrete",
            Category.Companion.DATAMUSE_API
        ),
        /*Category(
            "Brands",
            R.drawable.ctgry_brands,
            "words?rel_gen=brand",
            Category.Companion.DATAMUSE_API
        ) , */
        Category(
            "Names",
            R.drawable.ctgry_names,
            "na",
            Category.Companion.LIST_OF_NAMES_DATASET
        ),
        Category(
            "Short Words",
            R.drawable.ctgry_short_words,
            "words?sp=?????",
            Category.Companion.DATAMUSE_API
        ),
        Category(
            "Long Words",
            R.drawable.ctgry_long_words,
            "words?sp=??????????????",
            Category.Companion.DATAMUSE_API
        ),

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
                categoryList[position].name)
            categoryIntent.putExtra(PracticeCameraActivity.CATEGORY_ENDPOINT,
                categoryList[position].endpoint)
            categoryIntent.putExtra(PracticeCameraActivity.CATEGORY_API,
                categoryList[position].api)

            this.practiceCameraActivityLauncher.launch(categoryIntent)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}