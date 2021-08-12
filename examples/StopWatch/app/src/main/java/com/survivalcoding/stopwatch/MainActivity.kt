package com.survivalcoding.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.survivalcoding.stopwatch.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var time = 0
    private var timerTask: Timer? = null
    private var isRunning = false

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.fab.setOnClickListener {
            isRunning = !isRunning

            if (isRunning) {
                start()
            } else {
                pause()
            }
        }

    }

    private fun pause() {
        binding.fab
            .setImageResource(R.drawable.ic_baseline_play_arrow_24)
        timerTask?.cancel()
    }

    private fun start() {
        binding.fab.setImageResource(R.drawable.ic_baseline_pause_24)
        timerTask = timer(period = 10) {
            time++
            val sec = time / 100
            val milli = time % 100
            runOnUiThread {
                binding.secTextView.text = "$sec"
                binding.milliTextView.text = "$milli"
            }
        }
    }
}