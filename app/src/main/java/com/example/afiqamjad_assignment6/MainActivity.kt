package com.example.afiqamjad_assignment6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.afiqamjad_assignment6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameTop,BoggleFragment())
        fragmentTransaction.replace(R.id.frameBottom,ScoreFragment())
        fragmentTransaction.commit()
    }
}