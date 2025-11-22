package com.itismob.group8.aslfingerspellingapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object VideoPlayerHelper {
    fun playYouTubeVideo(context: Context, youtubeId: String) {
        try {
            // Try to open in YouTube app first
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$youtubeId"))
            intent.putExtra("force_fullscreen", true)

            // Check if YouTube app is installed
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                // Fallback to browser
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/watch?v=$youtubeId")
                )
                context.startActivity(webIntent)
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Error playing video", Toast.LENGTH_SHORT).show()
        }
    }
}