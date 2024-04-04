package com.example.afiqamjad_assignment6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.afiqamjad_assignment6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), Communicator {
    private lateinit var binding : ActivityMainBinding

    override fun startNewGame() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameTop,BoggleFragment())
        fragmentTransaction.commit()
    }

    override fun submitWord(submittedWord: String) {
        val scoreFragment = supportFragmentManager.findFragmentById(R.id.frameBottom) as? ScoreFragment
        scoreFragment?.receiveWord(submittedWord)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameTop,BoggleFragment())
        fragmentTransaction.replace(R.id.frameBottom, ScoreFragment())
        val scoreFragment = ScoreFragment()
        fragmentTransaction.add(R.id.frameBottom, scoreFragment, "scoreFragment")
        fragmentTransaction.commit()
    }
}