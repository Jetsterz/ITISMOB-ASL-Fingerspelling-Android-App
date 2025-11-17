package com.itismob.group8.aslfingerspellingapp.practicetranslate

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityPracticeCategoryBinding

class PracticeCategoryActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityPracticeCategoryBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PracticeCategoryAdapter

    private val practiceCameraResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPracticeCategoryBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        this.recyclerView = viewBinding.rvPracticeCategory
        this.adapter = PracticeCategoryAdapter(practiceCameraResultLauncher,
            this)
        this.recyclerView.adapter = this.adapter
        this.recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
