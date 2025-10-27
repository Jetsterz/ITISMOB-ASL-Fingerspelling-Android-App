package com.itismob.group8.aslfingerspellingapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.mediapipe.tasks.components.containers.Category
import com.itismob.group8.aslfingerspellingapp.databinding.TranslateItemGestureRecognizerResultBinding
import kotlin.math.min

class TranslateGestureRecognizerResultsAdapter: RecyclerView.Adapter<TranslateGestureRecognizerResultsAdapter.ViewHolder>() {
    companion object {
        private const val NO_VALUE = ""
    }

    private var adapterCategories: MutableList<Category?> = mutableListOf()
    private var adapterSize: Int = 0

    @SuppressLint("NotifyDataSetChanged")
    fun updateResults(categories: List<Category>?) {
        adapterCategories = MutableList(adapterSize) { null }
        if (categories != null) {
            val sortedCategories = categories.sortedByDescending { it.score() }
            val min = min(sortedCategories.size, adapterCategories.size)
            for (i in 0 until min) {
                adapterCategories[i] = sortedCategories[i]
            }
            adapterCategories.sortedBy { it?.index() }
            notifyDataSetChanged()
        }
    }

    fun updateAdapterSize(size: Int) {
        adapterSize = size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = TranslateItemGestureRecognizerResultBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        adapterCategories[position].let { category ->
            holder.bind(category?.categoryName())
        }
    }

    override fun getItemCount(): Int = adapterCategories.size

    inner class ViewHolder(private val binding: TranslateItemGestureRecognizerResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(label: String?) {
            with(binding) {
                tvTranslateResult.text = label ?: NO_VALUE
            }
        }
    }
}