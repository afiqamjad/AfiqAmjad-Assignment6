package com.example.afiqamjad_assignment6

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ScoreFragment : Fragment() {
    private val wordsUrl = "https://raw.githubusercontent.com/dwyl/english-words/master/words.txt"
    private var score: Int = 0
    private lateinit var scoreDisplay: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_score, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newGameButton = view.findViewById<Button>(R.id.newGame)
        scoreDisplay = view.findViewById(R.id.score)
        score = 0
        scoreDisplay.setText(score.toString())
        scoreDisplay.inputType = InputType.TYPE_NULL
        newGameButton.setOnClickListener { (activity as Communicator).startNewGame() }
    }

    fun receiveWord(submittedWord: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val wordsUrl = "https://raw.githubusercontent.com/dwyl/english-words/master/words.txt"
            val wordsList = URL(wordsUrl).readText().split("\n")
            var newScore = 0
            var usedDouble = false
            if (submittedWord.lowercase() in wordsList) {
                for (letter in submittedWord) {
                    if (letter !in listOf('A', 'E', 'I', 'O', 'U')) {
                        if (letter in listOf('S', 'Z', 'P', 'X', 'Q')) {
                            usedDouble = true
                        }
                        newScore++
                    } else {
                        newScore += 5
                    }
                }
                if (usedDouble) {
                    newScore *= 2
                }
            } else {
                newScore = -10
            }
            if (newScore < 0) {
                if (score <= 0) {
                    score = 0
                } else {
                    score -= newScore
                }
            } else {
                score += newScore
            }
            // Update UI on the main thread
            launch(Dispatchers.Main) {
                scoreDisplay.setText(score.toString())
            }
        }
    }
}