package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityMainBinding
import androidx.fragment.app.Fragment
class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        /* Set up the navbar */
        val translatePracticeFragment = TranslatePracticeFragment()
        val viewVideosFragment = ViewVideosFragment()
        val playFragment = PlayFragment()
        val dictionaryFragment = DictionaryFragment()

        setCurrentFragment(translatePracticeFragment)

        viewBinding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.translate -> setCurrentFragment(translatePracticeFragment)
                R.id.play -> setCurrentFragment(playFragment)
                R.id.viewSavedVideos -> setCurrentFragment(viewVideosFragment)
                R.id.dictionary -> setCurrentFragment(dictionaryFragment)
            }
            true
        }
    }

    /*
        Function for setting the fragment
     */
    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.flFragment, fragment)
            commit()
        }
    }


}
