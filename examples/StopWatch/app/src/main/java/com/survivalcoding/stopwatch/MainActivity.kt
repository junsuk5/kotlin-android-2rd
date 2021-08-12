package com.survivalcoding.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.survivalcoding.stopwatch.databinding.ActivityMainBinding
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private var time = 0    // ①
    private var timerTask: Timer? = null    // ②

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun start() {
        binding.fab.setImageResource(R.drawable.ic_baseline_pause_24)    // ③
        timerTask = timer(period = 10) {    // ④
            time++    // ⑤
            val sec = time / 100
            val milli = time % 100
            runOnUiThread {        // ⑥
                binding.secTextView.text = "$sec"
                binding.milliTextView.text = "$milli"
            }
        }
    }

    private fun pause() {
        binding.fab.setImageResource(R.drawable.ic_baseline_play_arrow_24)  // 1
        timerTask?.cancel()     // 2
    }
}