package com.example.afiqamjad_assignment6


import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlin.math.abs
import kotlin.random.Random


class BoggleFragment : Fragment() {


    private var prevPressedButtonIndex: Int = -1
    private var wordBeingGuessed: String = ""
    private lateinit var guesserText: EditText
    private lateinit var wordsSubmitted: MutableList<String>
    private var vowelCount: Int = 0


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
        val clearButton = view.findViewById<Button>(R.id.clearButton)
        val submitButton = view.findViewById<Button>(R.id.submitButton)
        guesserText = view.findViewById(R.id.wordGuessed)
        guesserText.inputType = InputType.TYPE_NULL
        wordBeingGuessed = ""
        prevPressedButtonIndex = -1
        wordsSubmitted = mutableListOf("Hello")
        wordsSubmitted.clear()
        vowelCount = 0
        guesserText.text.clear()


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


        var vowelRandomCount = 0
        var buttonCount = 0
        for (button in buttons) {
            var randomLetter = alphabet[Random.nextInt(0, 26)].toString()
            if ((buttonCount == buttons.size - 1 && vowelCount < 2) || (buttonCount == ((buttons.size - 1)/2) && vowelCount < 1)) {
                randomLetter = vowel[Random.nextInt(0, 5)].toString()
            }
            button.text = randomLetter


            if (randomLetter in listOf<String>("A", "E", "I", "O", "U")) {
                vowelRandomCount++
            }
            buttonCount++
        }

        clearButton.setOnClickListener{
            guesserText.text.clear()
            for (button in buttons) {
                button.setBackgroundResource(R.drawable.button)
                button.isEnabled = true
            }
            prevPressedButtonIndex = -1
            wordBeingGuessed = ""
            vowelCount = 0
        }

        for ((index, button) in buttons.withIndex()) {
            button.setOnClickListener {

                if (prevPressedButtonIndex != -1) {
                    // Calculate the row and column of the previously pressed button
                    val prevRow = prevPressedButtonIndex / 4
                    val prevCol = prevPressedButtonIndex % 4


                    // Calculate the row and column of the currently pressed button
                    val currRow = index / 4
                    val currCol = index % 4
                    if (abs(prevRow - currRow) <= 1 && abs(prevCol - currCol) <= 1) {
                        button.setBackgroundResource(R.color.gray)
                        button.isEnabled = false
                        wordBeingGuessed += button.text.toString()
                        guesserText.setText(wordBeingGuessed)
                        prevPressedButtonIndex = index
                        if (button.text.toString() in listOf("A", "E", "I", "O", "U")) {
                            vowelCount++
                        }
                    } else {
                        // Buttons are not adjacent, handle accordingly
                        Toast.makeText(context, "INVALID CHOICE", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    button.setBackgroundResource(R.color.gray)
                    button.isEnabled = false
                    wordBeingGuessed += button.text.toString()
                    guesserText.setText(wordBeingGuessed)
                    prevPressedButtonIndex = index
                    if (button.text.toString() in listOf("A", "E", "I", "O", "U")) {
                        vowelCount++
                    }
                }
            }
        }

        submitButton.setOnClickListener {
            if (wordBeingGuessed.isNotEmpty()) {
                if (wordBeingGuessed.length >= 4) {
                    if (wordBeingGuessed !in wordsSubmitted) {
                        if (vowelCount >= 2) {
                            (activity as Communicator).submitWord(wordBeingGuessed)
                            wordsSubmitted.add(wordBeingGuessed)
                            clearButton.performClick()
                        } else {
                            Toast.makeText(context, "Word needs at least 2 vowels!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "You've already guessed that word!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Word needs to be at least 4 characters long!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Word being guessed is empty!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

}

