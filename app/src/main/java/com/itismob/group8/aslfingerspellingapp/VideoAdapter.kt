package com.itismob.group8.aslfingerspellingapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itismob.group8.aslfingerspellingapp.databinding.ItemVideoCardBinding

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

        // Load YouTube thumbnail using Glide
        Glide.with(holder.itemView.context)
            .load(video.thumbnailUrl)
            .placeholder(R.drawable.asl_placeholder) // Your existing placeholder
            .into(holder.binding.imageThumbnail)

        // Click to play video
        holder.itemView.setOnClickListener {
            VideoPlayerHelper.playYouTubeVideo(holder.itemView.context, video.youtubeId)
        }
        
    }

    override fun getItemCount() = videos.size
}