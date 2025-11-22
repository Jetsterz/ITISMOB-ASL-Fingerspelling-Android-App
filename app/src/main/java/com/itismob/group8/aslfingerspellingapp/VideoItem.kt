package com.itismob.group8.aslfingerspellingapp

data class VideoItem(
    val title: String,
    val youtubeId: String,
    val thumbnailUrl: String = "https://img.youtube.com/vi/$youtubeId/hqdefault.jpg"
)