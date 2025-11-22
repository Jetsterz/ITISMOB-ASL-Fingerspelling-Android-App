package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.itismob.group8.aslfingerspellingapp.databinding.FragmentViewvideosBinding

class ViewVideosFragment : Fragment() {

    private var _binding: FragmentViewvideosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: VideoAdapter

    // YouTube video data
    private val youtubeVideos = listOf(
        VideoItem("Learn ASL Alphabet Video", "6_gXiBe9y9A"),
        VideoItem("Learn the ASL Alphabet Fast", "DBQINq0SsAw"),
        VideoItem("Alphabet Filipino Sign Language Tutorial", "iYpTJ5cEl9Y"),
        VideoItem("American Sign Language Alphabet", "cGavOVNDj1s"),
        VideoItem("The ASL Alphabet | American Sign Language - ABCs", "tkMg8g8vVUo")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewvideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView
        adapter = VideoAdapter(youtubeVideos)
        binding.recyclerViewVideos.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewVideos.adapter = adapter

        // Hide delete button since we're showing YouTube videos
        binding.buttonDelete.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}