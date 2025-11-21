package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityPlayCategoryBinding

class PlayCategoryActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityPlayCategoryBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlayCategoryAdapter

    private val playCameraResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPlayCategoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        this.recyclerView = viewBinding.rvPlayCategory
        this.adapter = PlayCategoryAdapter(playCameraResultLauncher,
            this)
        this.recyclerView.adapter = this.adapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
