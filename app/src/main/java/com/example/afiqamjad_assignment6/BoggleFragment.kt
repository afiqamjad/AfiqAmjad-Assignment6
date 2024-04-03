package com.example.afiqamjad_assignment6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlin.random.Random

class BoggleFragment : Fragment() {

    private val buttonIds = intArrayOf(
        R.id.button1, R.id.button2, R.id.button3, R.id.button4,
        R.id.button5, R.id.button6, R.id.button7, R.id.button8,
        R.id.button9, R.id.button10, R.id.button11, R.id.button12,
        R.id.button13, R.id.button14, R.id.button15, R.id.button16
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_boggle, container, false)

        val buttons = Array(buttonIds.size) { index ->
            view.findViewById<Button>(buttonIds[index])
        }

        val alphabet = arrayOf(
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        )

        val vowel = arrayOf(
            'A', 'E', 'I', 'O', 'U'
        )

        alphabet.shuffle()
        vowel.shuffle()

        var vowelCount = 0
        var buttonCount = 0
        for (button in buttons) {
            var randomLetter = alphabet[Random.nextInt(0, 26)].toString()
            if ((buttonCount == buttons.size - 1 && vowelCount < 2) || (buttonCount == ((buttons.size - 1)/2) && vowelCount < 1)) {
                randomLetter = vowel[Random.nextInt(0, 5)].toString()
            }
            button.text = randomLetter

            if (randomLetter in listOf<String>("A", "E", "I", "O", "U")) {
                vowelCount++
            }
            buttonCount++
        }
        return view
    }
}