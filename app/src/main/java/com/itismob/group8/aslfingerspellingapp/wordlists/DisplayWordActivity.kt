package com.itismob.group8.aslfingerspellingapp.wordlists

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityDisplayWordBinding

class DisplayWordActivity : AppCompatActivity() {
    private lateinit var b: ActivityDisplayWordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDisplayWordBinding.inflate(layoutInflater)
        val v = b.root
        setContentView(v)
    }
}