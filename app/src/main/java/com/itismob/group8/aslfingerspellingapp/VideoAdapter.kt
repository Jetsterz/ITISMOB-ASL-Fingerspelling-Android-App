package com.itismob.group8.aslfingerspellingapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ItemVideoCardBinding

data class VideoItem(val title: String)

class VideoAdapter(private val videos: List<VideoItem>) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(val binding: ItemVideoCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.binding.textVideoTitle.text = video.title

        // Placeholder click for "view video"
        holder.itemView.setOnClickListener {
            // TODO: Handle view action (open video)
        }

        holder.binding.checkboxSelect.setOnCheckedChangeListener { _, _ ->
            // TODO: Handle selection logic later
        }
    }

    override fun getItemCount() = videos.size
}
