package com.example.afiqamjad_assignment6

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import okhttp3.*
import java.io.IOException
import java.util.Locale
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ScoreFragment : Fragment(), SensorEventListener {
    private var score: Int = 0
    private lateinit var scoreDisplay: EditText
    private val client = OkHttpClient()
    private var dictionary: Set<String> = emptySet()
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var currentAcceleration = SensorManager.GRAVITY_EARTH
    private var lastAcceleration = SensorManager.GRAVITY_EARTH

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_score, container, false)
        scoreDisplay = view.findViewById(R.id.score)
        scoreDisplay.inputType = InputType.TYPE_NULL
        fetchDictionary()
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newGameButton = view.findViewById<Button>(R.id.newGame)
        newGameButton.setOnClickListener {
            (activity as Communicator).startNewGame()
            score = 0
            scoreDisplay.text.clear()
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        lastAcceleration = currentAcceleration
        currentAcceleration = sqrt(x * x + y * y + z * z)
        val delta = currentAcceleration - lastAcceleration

        if (delta > 2) {
            (activity as Communicator).startNewGame()
            score = 0
            scoreDisplay.text.clear()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
    }

    private fun fetchDictionary() {
        val request = Request.Builder()
            .url("https://raw.githubusercontent.com/dwyl/english-words/master/words.txt")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val words = response.body!!.string().split("\n")
                    dictionary = words.toSet()
                }
            }
        })
    }

    fun receiveWord(word: String) {
        if (isValid(word)) {
            val newScore = calculateScore(word)
            score += newScore
            Toast.makeText(context, "Correct! +$newScore", Toast.LENGTH_SHORT).show()
        } else {
            if (score < 10) {
                score = 0
            } else {
                score -= 10            }
            Toast.makeText(context, "Wrong! -10", Toast.LENGTH_SHORT).show()
        }
        scoreDisplay.setText(score.toString())
    }

    private fun isValid(word: String): Boolean {
        return dictionary.contains(word.lowercase(Locale.ROOT))
    }

    private fun calculateScore(word: String): Int {
        var score = 0
        var hasSpecialConsonant = false
        for (char in word) {
            when (char.lowercaseChar()) {
                'a', 'e', 'i', 'o', 'u' -> score += 5
                's', 'z', 'p', 'x', 'q' -> {
                    score += 1
                    hasSpecialConsonant = true
                }
                else -> score += 1
            }
        }
        if (hasSpecialConsonant) {
            score *= 2
        }
        return score
    }
}
