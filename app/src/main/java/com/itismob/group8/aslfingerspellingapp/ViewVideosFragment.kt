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
    private val videoList = mutableListOf<VideoItem>()

    private var isDataLoaded = false // ✅ Prevents repopulation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewvideosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup RecyclerView and adapter once
        adapter = VideoAdapter(videoList)
        binding.recyclerViewVideos.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerViewVideos.adapter = adapter

        // ✅ Only populate videos the first time
        if (!isDataLoaded) {
            videoList.addAll(
                listOf(
                    VideoItem("ASL Alphabet Tutorial"),
                    VideoItem("Basic Greetings in ASL"),
                    VideoItem("Common Phrases - Part 1"),
                    VideoItem("Fingerspelling Numbers 1-10"),
                    VideoItem("Fingerspelling Practice")
                )
            )
            adapter.notifyDataSetChanged()
            isDataLoaded = true
        }

        // Delete button (placeholder for now)
        binding.buttonDelete.setOnClickListener {
            // TODO: Delete logic later
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}