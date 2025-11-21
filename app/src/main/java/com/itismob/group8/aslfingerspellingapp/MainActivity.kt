package com.itismob.group8.aslfingerspellingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.itismob.group8.aslfingerspellingapp.databinding.ActivityMainBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private var activeFragment : Fragment = TranslatePracticeFragment()

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
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply{
                add(R.id.flFragment, dictionaryFragment, "4").hide(dictionaryFragment)
                add(R.id.flFragment, playFragment, "3").hide(playFragment)
                add(R.id.flFragment, viewVideosFragment, "2").hide(viewVideosFragment)
                add(R.id.flFragment, translatePracticeFragment, "1").hide(translatePracticeFragment)
            }.commit()
            supportFragmentManager.beginTransaction().show(translatePracticeFragment).commit()
        }

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
        supportFragmentManager.beginTransaction()
            .hide(activeFragment)
            .show(fragment)
            .commit()
        activeFragment = fragment
        }
}
